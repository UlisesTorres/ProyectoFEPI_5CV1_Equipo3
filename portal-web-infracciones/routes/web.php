<?php

use App\Http\Controllers\AdminController;
use App\Http\Controllers\CorralonController;
use App\Http\Controllers\InfocdmxController;
use App\Http\Controllers\InfraccionController;
use App\Http\Middleware\CheckRoleType;
use Illuminate\Foundation\Application;
use Illuminate\Support\Facades\Route;
use Inertia\Inertia;

Route::get('/', function () {
    return Inertia::render('Welcome', [
        'canLogin' => Route::has('login'),
        'canRegister' => Route::has('register'),
        'laravelVersion' => Application::VERSION,
        'phpVersion' => PHP_VERSION,
    ]);
});

Route::get('/infracciones', [InfraccionController::class, 'index'])
    ->name('infracciones.index');

Route::get('/infracciones/buscar', [InfraccionController::class, 'buscar'])
    ->name('infracciones.buscar');

Route::get('/infracciones/apelacion', [InfraccionController::class, 'crear'])
    ->name('infracciones.crear');

Route::get('/infracciones/generar-vista', [InfraccionController::class, 'previsualizar'])
    ->name('infracciones.previsualizar');

Route::get('/infracciones/pagos', [InfraccionController::class, 'consultarPagos'])
    ->name('pagos.index');

Route::get('/infracciones/pagos/buscar', [InfraccionController::class, 'buscarPagos'])
    ->name('pagos.buscar');

Route::post('/infracciones/solicitar-reimpresion', [InfraccionController::class, 'solicitarReimpresion'])
    ->name('pagos.solicitar-reimpresion');

Route::middleware([
    'auth:sanctum',
    config('jetstream.auth_session'),
    'verified',
])->group(function () {
    Route::get('/dashboard', function () {
        return Inertia::render('Dashboard');
    })->name('dashboard');

    Route::get('/infracciones/reportes', [InfraccionController::class, 'panelReportes'])
        ->name('infracciones.reportes');

    Route::get('/infracciones/generar-reporte-pdf', [InfraccionController::class, 'generarVistaReporte']);


    Route::get('/admin/dashboard', [AdminController::class, 'index'])
        ->name('admin.dashboard');

    Route::get('/corralon/dashboard', [CorralonController::class, 'index'])
        ->name('corralon.dashboard');

    Route::get('/infocdmx/dashboard', [InfoCdmxController::class, 'index'])
        ->name('infocdmx.dashboard');
});
