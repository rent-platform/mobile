package com.example.deals.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.deals.presentation.dealsguest.DealsGuestRoute

@Composable
fun DealsEntryRoute(
    isAuthorized: Boolean,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToDealDetails: (dealId: String) -> Unit,
    onNavigateToCreateListing: () -> Unit,) {
    if(isAuthorized){
        DealsRoute(
            modifier = modifier,
            onNavigateToDealDetails = onNavigateToDealDetails,
            onNavigateToCreateListing = onNavigateToCreateListing)
    }
    else{
        DealsGuestRoute( onNavigateToAuth = onLoginClick)
    }
}