ZK_BAN_PATH=.
ZK_BAN_AAR=zk-ban.aar
ZK_BAN_PACKAGE=github.com/akakou/zk-ban-system
ANDROID_API=23 

# export GOPATH=/home/akakou/go
# export GOMODCACHE=$GOPATH/pkg/mod

# gomobile clean
# gomobile init

cd $ZK_BAN_PATH
gomobile bind -o $ZK_BAN_AAR -target=android -androidapi $ANDROID_API .

# cd $CURRENT
# mv $ZK_BAN_PATH/$ZK_BAN_AAR .
