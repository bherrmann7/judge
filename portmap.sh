 sudo /sbin/iptables -t nat -I PREROUTING -p tcp --destination-port 80 -j REDIRECT --to-ports 3000
sudo /sbin/iptables -t nat -A OUTPUT -p tcp -d 192.168.1.10 --dport 80 -j  REDIRECT --to-port 3000

