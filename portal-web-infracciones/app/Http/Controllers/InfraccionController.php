<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use Inertia\Inertia;

class InfraccionController extends Controller
{
    /**
     * Vista inicial de consulta
     */
    public function index()
    {
        return Inertia::render('Infracciones/Consulta');
    }

    /**
     * Busca infracciones por placa
     */
    public function buscar(Request $request)
    {
        $request->validate([
            'placa' => 'required|string|min:3'
        ]);

        $placa = strtoupper($request->placa);

        $strapiUrl = config('services.strapi.url');
        $token = config('services.strapi.token');

        $endpoint = rtrim($strapiUrl, '/') . '/api/infraccions';

        $response = Http::withToken($token)
            ->timeout(10)
            ->get($endpoint, [
                'filters[placa_vehiculo][$eq]' => $placa,
                'populate' => '*',
            ]);

        if ($response->failed()) {
            return Inertia::render('Infracciones/Resultados', [
                'placa_consultada' => $placa,
                'lista_infracciones' => [],
                'strapi_url' => $strapiUrl,
                'error' => [
                    'status' => $response->status(),
                    'mensaje' => 'Strapi respondió con error',
                    'detalle' => $response->json(),
                ],
            ]);
        }

        // 7️⃣ Respuesta exitosa
        $data = $response->json();

        return Inertia::render('Infracciones/Resultados', [
            'placa_consultada' => $placa,
            'lista_infracciones' => $data['data'] ?? [],
            'strapi_url' => $strapiUrl,
        ]);
    }

    /**
     * Vista para crear reporte
     */
    public function crear()
    {
        return Inertia::render('FormularioReporte');
    }

    /**
     * Previsualización del reporte
     */
    public function previsualizar(Request $request)
    {
        $datos = $request->validate([
            'nombre_solicitante' => 'required|string',
            'placa_vehiculo'     => 'required|string',
            'folio_infraccion'   => 'required|string',
            'observaciones'      => 'nullable|string',
        ]);

        return view('formpreview', compact('datos'));
    }

    /**
     * Vista de consulta de pagos
     */
    public function consultarPagos()
    {
        return Inertia::render('Infracciones/ConsultaPagos');
    }

    /**
     * Busca pagos por folio
     */
    public function buscarPagos(Request $request)
    {
        $request->validate([
            'folio' => 'required|string|min:3'
        ]);

        $folio = strtoupper($request->folio);

        $strapiUrl = config('services.strapi.url');
        $token = config('services.strapi.token');

        $endpointInfracciones = rtrim($strapiUrl, '/') . '/api/infraccions';
        $endpointPagos = rtrim($strapiUrl, '/') . '/api/pagos';

        $responseInfraccion = Http::withToken($token)
            ->timeout(10)
            ->get($endpointInfracciones, [
                'filters[folio][$eq]' => $folio,
                'populate' => '*',
            ]);

        if ($responseInfraccion->failed()) {
            return Inertia::render('Infracciones/ResultadosPagos', [
                'folio_consultado' => $folio,
                'lista_pagos' => [],
                'infraccion' => null,
                'strapi_url' => $strapiUrl,
                'error' => 'No se pudo consultar la infracción',
            ]);
        }

        $dataInfraccion = $responseInfraccion->json();
        $infraccion = $dataInfraccion['data'][0] ?? null;

        if (!$infraccion) {
            return Inertia::render('Infracciones/ResultadosPagos', [
                'folio_consultado' => $folio,
                'lista_pagos' => [],
                'infraccion' => null,
                'strapi_url' => $strapiUrl,
            ]);
        }

        $responsePagos = Http::withToken($token)
            ->timeout(10)
            ->get($endpointPagos, [
                'filters[infraccion_id][id][$eq]' => $infraccion['id'],
                'populate' => '*',
            ]);

        if ($responsePagos->failed()) {
            return Inertia::render('Infracciones/ResultadosPagos', [
                'folio_consultado' => $folio,
                'lista_pagos' => [],
                'infraccion' => $infraccion,
                'strapi_url' => $strapiUrl,
                'error' => 'No se pudieron obtener los pagos',
            ]);
        }

        $dataPagos = $responsePagos->json();

        return Inertia::render('Infracciones/ResultadosPagos', [
            'folio_consultado' => $folio,
            'lista_pagos' => $dataPagos['data'] ?? [],
            'infraccion' => $infraccion,
            'strapi_url' => $strapiUrl,
        ]);
    }

    public function solicitarReimpresion(Request $request)
    {
        $validated = $request->validate([
            'linea_captura'    => 'required|string',
            'folio_infraccion' => 'required|string',
            'fecha_pago'       => 'required|date',
        ]);

        $strapiUrl = config('services.strapi.url');
        $token = config('services.strapi.token');

        $endpointBuscar = rtrim($strapiUrl, '/') . '/api/infraccions';

        $responseBuscar = Http::withToken($token)->get($endpointBuscar, [
            'filters[folio][$eq]' => $validated['folio_infraccion'],
            'fields[0]' => 'id', // Solo necesitamos el ID para optimizar
        ]);

        $dataBuscar = $responseBuscar->json();

        if ($responseBuscar->failed() || empty($dataBuscar['data'])) {
            return back()->withErrors(['error' => 'No se encontró la infracción original para vincular la solicitud.']);
        }

        // Obtenemos el ID real (ej: 1, 45, 102)
        $infraccionIdReal = $dataBuscar['data'][0]['id'];

        // 3. Crear la solicitud en Strapi usando el ID encontrado
        $endpointCrear = rtrim($strapiUrl, '/') . '/api/solicitud-reimpresions';

        $responseCrear = Http::withToken($token)->post($endpointCrear, [
            'data' => [
                'linea_captura' => $validated['linea_captura'],
                'infraccion_id' => $infraccionIdReal, // Aquí inyectamos el ID que Strapi necesita
                'fecha_pago'    => $validated['fecha_pago'],
            ]
        ]);

        if ($responseCrear->failed()) {
            return back()->withErrors(['error' => 'No se pudo registrar la solicitud en el sistema.']);
        }

        return back()->with('success', 'Solicitud de reimpresión enviada correctamente.');
    }

    /**
     * Vista del Panel de Reportes y Estadísticas
     */
    public function panelReportes()
    {
        $strapiUrl = config('services.strapi.url');
        $token = config('services.strapi.token');

        // 1. Obtener Infracciones para estadísticas (igual que antes)
        $endpointInfracciones = rtrim($strapiUrl, '/') . '/api/infraccions';
        $responseInfracciones = Http::withToken($token)->get($endpointInfracciones, [
            'pagination[pageSize]' => 1000,
            'populate' => '*',
        ]);
        $infracciones = collect($responseInfracciones->json()['data'] ?? []);

        // 2. NUEVO: Obtener Tipos de Infracción para el select
        $endpointTipos = rtrim($strapiUrl, '/') . '/api/tipo-infraccions';
        $responseTipos = Http::withToken($token)->get($endpointTipos, [
            'pagination[pageSize]' => 100, // Traer todos los tipos
        ]);
        $tiposInfraccion = collect($responseTipos->json()['data'] ?? [])
            ->map(fn($t) => [
                'id' => $t['id'],
                'nombre' => $t['nombre'] // Usamos el campo 'nombre' según tu captura
            ])->values();

        // Procesamiento de estadísticas (igual que antes)
        $porTipo = $infracciones->groupBy(fn($i) => $i['tipo_infraccion_id']['nombre'] ?? 'Desconocido')
            ->map->count()->sortDesc();
        $porMedio = $infracciones->groupBy('medio_infraccion')->map->count()->sortDesc();

        return Inertia::render('PanelReportes', [
            'stats' => [
                'labels_tipo' => $porTipo->keys()->all(),
                'data_tipo' => $porTipo->values()->all(),
                'labels_medio' => $porMedio->keys()->all(),
                'data_medio' => $porMedio->values()->all(),
            ],
            'tipos_infraccion' => $tiposInfraccion // Enviamos la lista a la vista
        ]);
    }

    public function generarVistaReporte(Request $request)
    {
        $request->validate([
            'mes_inicio' => 'required|integer|min:1|max:12',
            'mes_fin'    => 'required|integer|min:1|max:12|gte:mes_inicio',
            'tipo_id'    => 'nullable|integer'
        ]);

        $strapiUrl = config('services.strapi.url');
        $token = config('services.strapi.token');
        $endpoint = rtrim($strapiUrl, '/') . '/api/infraccions';

        // --- CORRECCIÓN AQUÍ: Convertir a enteros explícitamente ---
        $mesInicio = (int) $request->mes_inicio;
        $mesFin = (int) $request->mes_fin;
        $anioActual = now()->year;

        // Usamos las variables casteadas ($mesInicio, $mesFin) en lugar de $request->...
        $inicio = Carbon::createFromDate($anioActual, $mesInicio, 1)->startOfMonth()->toIso8601String();
        $fin = Carbon::createFromDate($anioActual, $mesFin, 1)->endOfMonth()->toIso8601String();

        // 2. Construir Filtros
        $queryParams = [
            'populate' => '*',
            'pagination[pageSize]' => 500,
            'filters[fecha_infraccion][$gte]' => $inicio,
            'filters[fecha_infraccion][$lte]' => $fin,
        ];

        if ($request->tipo_id) {
            $queryParams['filters[tipo_infraccion_id][id][$eq]'] = $request->tipo_id;
        }

        $response = Http::withToken($token)->get($endpoint, $queryParams);
        $datos = $response->json()['data'] ?? [];

        // 3. Generar Título Dinámico (Usando las variables casteadas)
        $nombreMesInicio = Carbon::create()->month($mesInicio)->locale('es')->monthName;
        $nombreMesFin = Carbon::create()->month($mesFin)->locale('es')->monthName;

        $titulo = "Reporte de Infracciones: " . ucfirst($nombreMesInicio) . " a " . ucfirst($nombreMesFin) . " " . $anioActual;

        if ($request->tipo_id) {
            $nombreTipo = $datos[0]['tipo_infraccion_id']['nombre'] ?? 'Filtrado por Tipo';
            $titulo .= " (" . $nombreTipo . ")";
        }

        return view('reporteinfraccion', compact('datos', 'titulo'));
    }
}
