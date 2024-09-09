package no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.bottomsheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen.MapViewModel
import no.uio.ifi.in2000.team32.prosjekt.ui.theme.DarkBlue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BottomSheet(
    mapViewModel: MapViewModel, navController: NavController, showBottomSheet: MutableState<Boolean>
) {
    val numberOfPages = 2
    val pagerState = rememberPagerState(initialPage = 0) { numberOfPages }
    val coroutineScope = rememberCoroutineScope()

    val textColor = if (isSystemInDarkTheme()) Color.Unspecified else DarkBlue


    ModalBottomSheet(onDismissRequest = { showBottomSheet.value = false }, content = {
        Column(
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)    //Gjør slik at page-indicator-dots alltid ligger over doc eller hjemikon
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(700.dp)  // Justert høyde for å passe begge innholdstyper
            ) { page ->
                when (page) {
                    0 -> TableSheet(mapViewModel, navController, textColor)
                    1 -> GraphSheet(mapViewModel, navController, textColor)
                }
            }

            // Pager indicator dots
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(numberOfPages) { page ->
                    val currentPageDotColor = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                    val otherPageDotColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

                    val color =
                        if (pagerState.currentPage == page) currentPageDotColor else otherPageDotColor
                    Box(modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(12.dp)
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page)
                            }
                        })
                }
            }
        }
    })
}