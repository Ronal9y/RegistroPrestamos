package edu.ucne.registroprestamos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registroprestamos.presentation.Prestamos.PrestamoDeleteScreen
import edu.ucne.registroprestamos.presentation.Prestamos.PrestamoEditScreen
import edu.ucne.registroprestamos.presentation.Prestamos.PrestamoListScreen
import edu.ucne.registroprestamos.presentation.Prestamos.PrestamoScreen

@Composable
fun PrestamosNavHost(
    nav: NavHostController
) {
    NavHost(navController = nav, startDestination = Screen.List) {
        composable<Screen.List> {
            PrestamoListScreen(
                onAdd = { nav.navigate(Screen.Register) },
                onEdit = { id -> nav.navigate(Screen.Edit(id)) },
                onDelete = { id -> nav.navigate(Screen.Delete(id)) }
            )
        }

        composable<Screen.Register> {
            PrestamoScreen(
                prestamoId = null,
                goBack = { nav.popBackStack() }
            )
        }

        composable<Screen.Edit> {
            val args = it.toRoute<Screen.Edit>()
            PrestamoEditScreen(
                prestamoId = args.prestamoId,
                goBack = { nav.popBackStack() }
            )
        }

        composable<Screen.Delete> {
            val args = it.toRoute<Screen.Delete>()
            PrestamoDeleteScreen(
                prestamoId = args.prestamoId,
                goBack = { nav.popBackStack() }
            )
        }
    }
}