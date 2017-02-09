import sys
import crypt

input_name = sys.argv[1];
input_pass = sys.argv[2];

#print input_name,input_pass

line=tuple(open('/etc/shadow', 'r'))
use_pass ={}

for i in line:
	user_name = i.split(":",5)[0]
	pswd = i.split(":",5)[1]
	use_pass[user_name]=pswd;

if use_pass.has_key(input_name):
	store=use_pass[input_name].split('$')
	salt = '$'+store[1]+'$'+store[2]+'$'
	hashed_key =crypt.crypt(input_pass,salt)
	if (hashed_key == use_pass[input_name]):
		print salt
	else:
		print "****"
else:
	print "****"
