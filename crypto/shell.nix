let pkgs = import <nixpkgs> {};

in pkgs.mkShell rec {
  name = "zk-ban-android-dev";

  buildInputs = with pkgs; [
    android-tools
    go gomobile
    jdk gcc
  ];

  shellHook = ''
    export ALLOW_NINJA_ENV=true
    export USE_CCACHE=1
    export ANDROID_JAVA_HOME=${pkgs.jdk.home}sdkmanager install avd
    export LD_LIBRARY_PATH=/usr/lib:/usr/lib32
    export ANDROID_API=23 
    export GOPATH=$HOME/go
    export ZK_BAN_BENCH_PATH=.
    export ZK_BAN_AAR=zk-ban.aar
    export ZK_BAN_PACKAGE=github.com/akakou/zk-ban-system

    gomobile clean
    gomobile init
    gomobile bind -o $ZK_BAN_AAR -target=android -androidapi $ANDROID_API .
  '';
}

