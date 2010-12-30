#!/usr/bin/ruby

file_name = ARGV[0]
player_no = ARGV[1].to_i
class_name = file_name.split('_').map {|word| word.capitalize}.join

fail unless file_name =~ /[A-z_]*/
fail unless player_no == 1 or player_no == 2

load "#{file_name}.rb"
Object.const_get(class_name).new player_no

