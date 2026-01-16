<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>{{ $titulo }}</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* Regla para forzar la orientación HORIZONTAL */
        @page {
            size: landscape;
            margin: 1cm; /* Márgenes sugeridos */
        }

        @media print {
            body {
                -webkit-print-color-adjust: exact;
                print-color-adjust: exact;
            }
        }
    </style>
</head>
<body class="bg-white p-4 text-gray-800">

<!-- Encabezado del Reporte -->
<div class="border-b-2 border-red-600 pb-4 mb-6 flex justify-between items-center">
    <div class="flex items-center gap-4">
        <!-- Puedes agregar tu logo aquí si gustas -->
        <div>
            <h1 class="text-2xl font-bold text-gray-900 uppercase">Reporte de Infracciones</h1>
            <p class="text-red-600 font-medium text-lg">{{ $titulo }}</p>
        </div>
    </div>
    <div class="text-right text-sm text-gray-500">
        <p>Fecha de generación:</p>
        <p class="font-bold">{{ now()->format('d/m/Y H:i') }}</p>
    </div>
</div>

<!-- Resumen Rápido -->
<div class="mb-6 bg-gray-50 p-4 rounded-lg border border-gray-200 flex justify-between items-center">
    <p class="text-sm font-bold text-gray-700">Total de registros encontrados: <span class="text-red-600 text-lg">{{ count($datos) }}</span></p>
    <p class="text-xs text-gray-400">Formato Horizontal</p>
</div>

<!-- Tabla de Datos (Aprovechando el ancho horizontal) -->
<div class="w-full">
    <table class="w-full text-xs text-left border-collapse">
        <thead class="bg-gray-100 uppercase font-bold text-gray-600 border-b-2 border-gray-300">
        <tr>
            <th class="px-4 py-3 border-r border-gray-200">Folio</th>
            <th class="px-4 py-3 border-r border-gray-200">Fecha</th>
            <th class="px-4 py-3 border-r border-gray-200">Placa</th>
            <th class="px-4 py-3 border-r border-gray-200">Tipo Infracción</th>
            <th class="px-4 py-3 border-r border-gray-200 w-1/4">Ubicación</th> <!-- Más ancho para ubicación -->
            <th class="px-4 py-3 border-r border-gray-200">Medio</th>
            <th class="px-4 py-3">Oficial ID</th>
        </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
        @forelse($datos as $infraccion)
            <tr class="hover:bg-gray-50 break-inside-avoid"> <!-- break-inside-avoid evita que una fila se corte entre páginas -->
                <td class="px-4 py-2 font-medium border-r border-gray-100">{{ $infraccion['folio'] ?? 'S/N' }}</td>
                <td class="px-4 py-2 border-r border-gray-100 whitespace-nowrap">
                    {{ \Carbon\Carbon::parse($infraccion['fecha_infraccion'])->format('d/m/Y H:i') }}
                </td>
                <td class="px-4 py-2 font-bold border-r border-gray-100">{{ $infraccion['placa_vehiculo'] ?? '-' }}</td>
                <td class="px-4 py-2 border-r border-gray-100">
                    {{ $infraccion['tipo_infraccion_id']['nombre'] ?? 'No especificado' }}
                </td>
                <td class="px-4 py-2 border-r border-gray-100 truncate max-w-xs">
                    {{ $infraccion['ubicacion_infraccion'] ?? '-' }}
                </td>
                <td class="px-4 py-2 border-r border-gray-100">
                            <span class="px-2 py-1 rounded-full bg-gray-100 text-gray-600 text-[10px] border border-gray-200">
                                {{ $infraccion['medio_infraccion'] ?? 'N/A' }}
                            </span>
                </td>
                <td class="px-4 py-2 text-gray-500">
                    {{ $infraccion['oficial_id'] ?? 'N/A' }}
                </td>
            </tr>
        @empty
            <tr>
                <td colspan="7" class="px-4 py-12 text-center text-gray-500 italic bg-gray-50 rounded-lg mt-4">
                    No se encontraron infracciones registradas en este periodo.
                </td>
            </tr>
        @endforelse
        </tbody>
    </table>
</div>

<!-- Pie de página -->
<div class="mt-8 pt-4 border-t border-gray-200 text-center text-xs text-gray-400 flex justify-between">
    <span>Sistema de Gestión de Infracciones</span>
    <span>Página generada automáticamente</span>
</div>

</body>
</html>
