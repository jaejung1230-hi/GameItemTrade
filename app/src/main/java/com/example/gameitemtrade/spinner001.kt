package com.example.gameitemtrade

enum class game_array(val games: String){
    메이플스토리("maplestory_server_array"),
    로스트아크("lostark_server_array"),
    던전앤파이터("dungunandfighter_server_array"),
}

enum class maplestory_server_array(){
    오로라(),
    루나(),
    스카니아(),
    엘리시움(),
    크로아(),
}

enum class lostark_server_array(){
    루페온(),
    실리안(),
    아만(),
    카제로스(),
}

enum class dungunandfighter_server_array(){
    카인(),
    힐더(),
    안톤(),
    디레지에(),
    바칼(),
}

enum class report_array(){
    욕설_비매너(),
    판매글의_내용과_달라요(),
    물건을_못받았어요(),
    기타(),
}