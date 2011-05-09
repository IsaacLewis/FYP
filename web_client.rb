require 'socket'
require 'client.rb'
require 'card.rb'

class WebClient < Client
  @@port = 2020
  @@sleep_time = 0.5

  def initialize(player_no, quiet=false)
    @received_msgs = []
    @board = []
    @hand = []
    @opponent_hand = []
    @take_input = false
    @player_no = player_no
    Thread.new {start_web_server}
    sleep 1 while @name.nil?
    super(player_no)
  end

  def receive(str)
    sleep @@sleep_time
    @received_msgs << str.strip
    case str
    when /Hand No/
      @board = []
      @hand = []
      @opponent_hand = []
    when /is dealt .* \((..),(..)\)/
      @hand << $1
      @hand << $2
      @opponent_hand = ["??","??"]
    when /\((..)\) is dealt to the board/
      @board << $1
    when /Two players have connected\. ([A-z0-9]*) and ([A-z0-9]*)/
      if @player_no == 1
        @player_name = $1
        @opponent_name = $2
      else
        @player_name = $2
        @opponent_name = $1
      end
      
    when /Chips: ([0-9,]*)/
      chips = $1.split ','
      chips.reverse! if @player_no == 2
      @player_chips, @player_bet, @pot, @opponent_bet, @opponent_chips = 
        chips.map &:to_i

    when /([A-z0-9]*) shows (..) (..)/
      @opponent_hand = [$2,$3] if $1 == @opponent_name
      # sleep @@sleep_time * 10
    end
  end

  def get_action(str)
    @received_msgs << str.strip
    str.match /call \(([0-9]*)\), bet\/\(r\)aise \(([0-9]*)\)/
    @call_cost, @raise_cost = $1.to_i, $2.to_i
    @take_input = true
    until not $input.nil? and ["c","r","f","b"].include? $input
      sleep 1
    end 
    @take_input = false
    input = $input
    $input = nil
    return input
  end

  def start_web_server
    @web_server = TCPServer.new 'localhost', @@port
    while true 
      begin
        session = @web_server.accept
        process_request session
      rescue Exception => e
        puts e
      end
    end
  end

  def http_header(type)
    "HTTP/1.1 200/OK\r\nContent-type: #{type}\r\n\r\n"
  end

  def process_request(session)
    headers = session.recvfrom(1024)[0]
    uri = headers.match(/[(POST)|(GET)] (\/[a-z.]*)/)[1]
    
    case uri
    when "/info.txt"
      session.print http_header "text/html"
      session.print info_box
    when "/board.txt"
      session.print http_header "text/html"
      session.print board_box
    when "/styles.css"
      session.print http_header "text/css"
      session.print stylesheet
    else

      if @take_input and headers =~ /action=([a-z]*)/
        puts $1
        $input = $1.slice 0,1
      end
      
      session.print http_header "text/html"
      if @name.nil?
        if headers =~ /name=([A-z0-9]*)/
          @name = $1
          session.print main_page
        else
          session.print input_name_page
        end
      else
        session.print main_page
      end
    end
    session.close
  end

  private

  def form_button(title, cost=0)
    <<HTML
<form method='POST' action='/' class="action_form">
  <input type='hidden' name='action' value='#{title}' />
  <input class="action_button" type='submit' value='#{title.capitalize}#{" (#{cost} chips)" unless cost == 0}' />
</form>
HTML
  end

  def info_box
    html = @received_msgs.select do |msg| 
      msg !~ /Chips:/ and msg !~ /your move/
    end.last(25).join "\n<br>\n"

    if @take_input
      html += "<br><br/><br><b>#{@name}, your move?<b><br/>" +
        form_button(@call_cost == 0 ? "check" : "call", @call_cost) + 
        form_button((@player_bet == 0 and @opponent_bet == 0) ? "bet" : "raise", @raise_cost) + 
        form_button("fold")
    end
    html
  end

  def card_html(card)
    if card =~ /.[h|d]/
      color = "f11"
    else
      color = "111"
    end

    display = card.sub(/[h|s|c|d]/) do |suit| 
      {"h"=>"&hearts;","c"=>"&clubs;","d"=>"&diams;","s"=>"&spades;"}[suit]
    end

    "<div class=\"card\" style=\"color:##{color}\">#{display}</div>"
  end

  def chips_html(amt)
    amt = amt.to_i
    size = case amt
             when (0..10) then "micro"
             when (11..20) then "small"
             when (21..50) then "medium"
             else "big"
           end
    if amt == 0
      ""
    else
      "<div class=\"chips #{size}\">#{amt}</div>"
    end
  end

  def stylesheet
    File.read "web/styles.css"
  end

  def board_box
    <<HTML
<div class="name_container">
#{@opponent_name} #{chips_html @opponent_chips}
</div>

<div class="card-container">
#{chips_html @opponent_bet}
#{@opponent_hand.map {|card| card_html(card)}.join}
</div>

<div class="card-container">
#{chips_html @pot}
#{@board.map {|card| card_html(card)}.join}
</div>

<div class="card-container">
#{chips_html @player_bet}
#{@hand.map {|card| card_html(card)}.join}
</div>

<div class="name_container">
#{@player_name} #{chips_html @player_chips}
</div>
HTML

  end

  def input_name_page
    File.read "web/input_name.html"
  end

  def main_page
    File.read("web/main.html").sub "SLEEP_TIME", (@@sleep_time * 1000).to_s
  end
end
