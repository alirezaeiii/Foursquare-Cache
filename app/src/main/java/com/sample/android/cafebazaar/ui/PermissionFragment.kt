package com.sample.android.cafebazaar.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sample.android.cafebazaar.R
import com.sample.android.cafebazaar.util.composeView
import com.sample.android.cafebazaar.viewmodels.PermissionViewModel
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class PermissionFragment @Inject
constructor() // Required empty public constructor
    : DaggerFragment() {

    private lateinit var viewModel: PermissionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeView {
        viewModel = ViewModelProvider(this).get(PermissionViewModel::class.java)
        when (viewModel.mutableState.collectAsState().value) {
            PermissionViewModel.State.PERMISSION_DIALOG -> {
                Timber.i("Check for permission")
                PermissionDialog()
            }
            PermissionViewModel.State.PERMISSION_DENIED -> {
                Timber.i("Display permission denied screen")
                PermissionDenied()
            }
            PermissionViewModel.State.NEXT_SCREEN -> {
                Timber.i("Navigate to main screen")
                val action =
                    PermissionFragmentDirections.actionPermissionFragmentToMainFragment()
                findNavController().navigate(action)
            }
        }
    }

    @Composable
    private fun PermissionDialog() {
        val context = LocalContext.current
        val permissionStatus =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        if (PackageManager.PERMISSION_GRANTED == permissionStatus) {
            LaunchedEffect(Unit) {
                Timber.i("Permission was granted")
                viewModel.setState(PermissionViewModel.State.NEXT_SCREEN)
            }
        } else {
            RequestPermission()
        }
    }

    @Composable
    private fun RequestPermission() {
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                Timber.i("Permission is granted")
                lifecycleScope.launch {
                    viewModel.setState(PermissionViewModel.State.NEXT_SCREEN)
                }
            } else {
                Timber.i("Permission is not granted")
                viewModel.setState(PermissionViewModel.State.PERMISSION_DENIED)
            }
        }
        LaunchedEffect(Unit) {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @Composable
    private fun PermissionDenied() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .wrapContentSize(Alignment.Center)
        ) {
            Text(
                stringResource(id = R.string.location_permission_not_granted),
                color = Color.DarkGray,
                modifier = Modifier.padding(8.dp),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}