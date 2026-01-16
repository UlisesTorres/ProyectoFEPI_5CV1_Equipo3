<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Solicitud de Apelación</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* Estilos para pantalla */
        body {
            background-color: #e5e7eb;
            font-family: Arial, sans-serif;
            padding: 20px;
            font-size: 14px;
        }
        .hoja {
            background: white;
            width: 100%;
            max-width: 210mm;
            min-height: 297mm;
            margin: 0 auto;
            padding: 15mm;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
        }
        h1 { font-size: 18px; }
        h2 { font-size: 13px; }
        .texto-dato { font-size: 14px; }
        .texto-folio { font-size: 15px; }
        .texto-small { font-size: 11px; }
        .texto-xs { font-size: 10px; }

        /* Estilos para impresión */
        @media print {
            @page {
                /* AQUÍ ESTÁ EL CAMBIO CLAVE: Forzamos A4 y Vertical (Portrait) */
                size: A4 portrait;
                margin: 10mm;
            }
            body {
                background: white;
                margin: 0;
                padding: 0;
                font-size: 11px;
                -webkit-print-color-adjust: exact;
                print-color-adjust: exact;
            }
            .hoja {
                box-shadow: none;
                margin: 0;
                padding: 5mm;
                width: 100%;
                max-width: 100%;
                min-height: auto;
            }
            /* Ajustes de tamaño de fuente para impresión */
            h1 { font-size: 14px !important; }
            h2 { font-size: 11px !important; }
            .texto-dato { font-size: 11px !important; }
            .texto-folio { font-size: 12px !important; }
            .texto-small { font-size: 9px !important; }
            .texto-xs { font-size: 8px !important; }
            .mb-8 { margin-bottom: 12px !important; }
            .mb-12 { margin-bottom: 16px !important; }
            .mt-20 { margin-top: 30px !important; }
            .mt-16 { margin-top: 20px !important; }
            .pb-4 { padding-bottom: 8px !important; }
            .p-4 { padding: 8px !important; }
        }
    </style>
</head>
<body>

<div class="hoja">
    <!-- Encabezado -->
    <div class="text-center border-b-2 border-gray-800 pb-4 mb-8">
        <h1 class="font-bold">SOLICITUD DE APELACIÓN</h1>
        <p class="text-gray-600 texto-small">Departamento de Tránsito</p>
        <p class="texto-xs text-gray-500 mt-1">Fecha: {{ date('d/m/Y') }}</p>
    </div>

    <!-- Datos del Solicitante -->
    <div class="mb-8">
        <h2 class="font-bold text-gray-700 border-b border-gray-300 pb-2 mb-3">DATOS DEL SOLICITANTE</h2>
        <div class="grid grid-cols-2 gap-4 texto-dato">
            <div>
                <span class="font-bold block text-gray-500 texto-xs">NOMBRE DEL SOLICITANTE:</span>
                {{ $datos['nombre_solicitante'] }}
            </div>
            <div>
                <span class="font-bold block text-gray-500 texto-xs">PLACA DEL VEHÍCULO:</span>
                {{ $datos['placa_vehiculo'] }}
            </div>
        </div>
    </div>

    <!-- Datos de la Infracción -->
    <div class="mb-8">
        <h2 class="font-bold text-gray-700 border-b border-gray-300 pb-2 mb-3">DATOS DE LA INFRACCIÓN</h2>
        <div>
            <span class="font-bold block text-gray-500 texto-xs">FOLIO DE INFRACCIÓN:</span>
            <span class="texto-folio font-mono">{{ $datos['folio_infraccion'] }}</span>
        </div>
    </div>

    <!-- Motivo de Apelación -->
    @if(!empty($datos['observaciones']))
        <div class="mb-8">
            <h2 class="font-bold text-gray-700 border-b border-gray-300 pb-2 mb-3">MOTIVO DE LA APELACIÓN</h2>
            <div class="p-4 bg-gray-50 border border-gray-200 rounded texto-dato">
                <p>{{ $datos['observaciones'] }}</p>
            </div>
        </div>
    @endif

    <!-- Declaración -->
    <div class="mb-12 p-4 bg-yellow-50 border border-yellow-200 rounded texto-small">
        <p class="text-gray-700">
            Declaro bajo protesta de decir verdad que los datos proporcionados son verídicos y que solicito
            formalmente la revisión y apelación de la infracción antes mencionada.
        </p>
    </div>

    <!-- Firma -->
    <div class="mt-20 flex justify-center">
        <div class="text-center">
            <div class="border-t border-black w-48 pt-2 texto-small">Firma del Solicitante</div>
            <p class="texto-xs text-gray-500 mt-1">{{ $datos['nombre_solicitante'] }}</p>
        </div>
    </div>

    <!-- Pie de página -->
    <div class="mt-16 pt-4 border-t border-gray-200 text-center texto-xs text-gray-400">
        <p>Este documento es una solicitud formal de apelación y no garantiza la resolución favorable del caso.</p>
        <p>Documento generado el {{ date('d/m/Y H:i:s') }}</p>
    </div>
</div>

</body>
</html>
