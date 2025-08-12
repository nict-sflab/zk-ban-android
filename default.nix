let pkgs = import <nixpkgs> {};

in pkgs.mkShell rec {
  name = "passprover-dev";

  buildInputs = with pkgs; [
    # android-studio
    android-tools
    go gomobile
    jdk gcc
  ];

  runScript = "bash";
  profile = ''
    export ALLOW_NINJA_ENV=true
    export GOPATH=/home/akakou/go
    export USE_CCACHE=1
    export ANDROID_JAVA_HOME=${pkgs.jdk.home}sdkmanager install avd
    export LD_LIBRARY_PATH=/usr/lib:/usr/lib32
  '';
}
