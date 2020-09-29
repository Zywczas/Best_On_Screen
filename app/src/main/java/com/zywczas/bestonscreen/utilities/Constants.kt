package com.zywczas.bestonscreen.utilities

const val EXTRA_MOVIE = "movie"
const val EXTRA_CATEGORY = "category"
const val CONFIGURATION_CHANGE = "configuration change"
const val CONNECTION_PROBLEM = "Problem with internet. Check your connection and try again."

//todo pousuwac wszystkie logi

//todo to mi da wszystko biale na pasku
// <style name="ToolbarTheme" parent="ThemeOverlay.MaterialComponents.ActionBar">
//        <item name="android:textColorPrimary">@android:color/white</item>
//    </style>


//todo porownac rozne popUpTo w nav graph i sprawdzic back stack
//<!--    te animacje z POP to sa dla ruchow z backstack (dla powrotow do poprzednich fragmentow),
//animacje BEZ pop dla dla przechodzenia do nowego fragmentu (jeszcze go nie ma na backstack),
//animacje sa przypisane do akcji, nie do fragmentu bezposrednio,
//podczas jednego klikniecia mamy w ponizszym przypadku dwie animacje,
//1 animacja dla fragmentu ktory znika (exit) a druga(enter) dla fragmentu ktory przyhodzi,
//potrzebne sa 4 animacje tak jak tutaj pokazane:
//- 2 dla przechodzenia do drugiego fragmentu (enterAnim i exitAnim)
//- 2 inne dla powrotu ta sama akcja do pierwszego fragmentu (popEnterAnim i popExitAnim)
//-->

//todo backstack:
//jak damy w jakims action popUpto i inclusive to uzywajac tego action czyszcze caly backstack

//todo dac cos co zapobiegnie resetowaniu sie fragmentow przy minimalizowaniu aplikacji