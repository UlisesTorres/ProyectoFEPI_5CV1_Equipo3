<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Inertia\Inertia;

class CorralonController extends Controller
{
    public function index()
    {
        return Inertia::render('Corralon/Dashboard');
    }
}
