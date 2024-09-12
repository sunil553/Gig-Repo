package com.llyod.task.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.llyod.domain.entity.country.CountryListItem
import com.llyod.task.R
import com.llyod.task.viewmodel.CountryListViewModel


/**
 *  display country list
 *
 */
@Composable
fun CountryListScreen() {
    val viewModel: CountryListViewModel = hiltViewModel()
    Scaffold(
        topBar = {
            AppToolbar(toolbarTitle = stringResource(id = R.string.title))
        },
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            if (viewModel.loading.collectAsState().value){
                IndeterminateCircularIndicator()
            } else{
                val collectAsState = viewModel.countryListFlow.collectAsState()
                if (collectAsState.value.isNotEmpty()){
                    CountryListInfo(collectAsState.value)
                }
            }


        }
    }

}

@Composable
fun IndeterminateCircularIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(30.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 20.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ), color = colorResource(id = R.color.black),
        textAlign = TextAlign.Center
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppToolbar(toolbarTitle : String) {
    TopAppBar(title = {
        NormalTextComponent(value = toolbarTitle)
    } , colors =
    TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer))
}


/**
 * displays all  items in the lazy column
 *
 * @param countryList
 */
@Composable
fun CountryListInfo(countryList : List<CountryListItem> ) {
    LazyColumn {
        itemsIndexed(items = countryList) { index, item ->
            CountryItem(item)
        }
    }
}

@Composable
fun CountryItem(item: CountryListItem){
    Card(modifier = Modifier
        .padding(9.dp)
        .fillMaxWidth(),
        border = BorderStroke(0.1.dp, Color.Black)
    ) {
        Surface {
            Row {
                Column(verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxHeight()
                        .weight(0.4f)) {
                    NormalTextComponent(item.LocalizedName)
                }
            }
        }

    }

}