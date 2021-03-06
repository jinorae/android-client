package com.podevs.android.poAndroid;

public enum Command {
    ZipCommand, //= 0,
    Login,
    Reconnect,
    Logout,
    SendMessage,
    PlayersList,
    SendTeam, // Name Change
    ChallengeStuff,
    EngageBattle,
    BattleFinished,
    BattleMessage, // = 10,
    BattleChat,
    KeepAlive, /* obsolete since we use a native Qt option now */
    AskForPass,
    Register,
    PlayerKick,
    PlayerBan,
    ServerInfoChanged,
    Unused_18,
    Unused_19,
    SendPM, // = 20,
    OptionsChanged,
    GetUserInfo,
    GetUserAlias,
    GetBanList,
    CPBan,
    CPUnban,
    SpectateBattle,
    SpectateBattleMessage,
    SpectateBattleChat,
    Unused_30, // = 30,
    Unused_31,
    Cookie,
    VersionControl,
    TierSelection,
    Unused_35,
    FindBattle,
    ShowRankings,
    Announcement,
    CPTBan,
    Unused_40, // = 40,
    PlayerTBan,
    Unused_42,
    BattleList,
    ChannelsList,
    ChannelPlayers,
    JoinChannel,
    LeaveChannel,
    ChannelBattle,
    RemoveChannel,
    AddChannel, // = 50,
    Unused_51,
    ChanNameChange,
    Unused_53,
    Unused_54,
    Unused_55,
    SpecialPass,
    ServerListEnd,               // Indicates end of transmission for registry.
    Unused_58,
    ServerPassword // 59
}