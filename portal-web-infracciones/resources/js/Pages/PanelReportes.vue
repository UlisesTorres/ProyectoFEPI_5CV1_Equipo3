<script setup>
import { ref, computed, nextTick } from 'vue';
import PublicLayout from "@/Layouts/PublicLayout.vue";
import { Pie } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, ArcElement);

const props = defineProps({
    stats: Object,
    tipos_infraccion: Array
});

// ... (Configuración de gráficas se mantiene igual) ...
const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: { position: 'bottom', labels: { color: '#6b7280', font: { size: 12 } } },
        tooltip: { backgroundColor: 'rgba(0,0,0,0.8)', titleColor: '#fff', bodyColor: '#fff' }
    }
};
const backgroundColors = ['#ef4444', '#3b82f6', '#10b981', '#f59e0b', '#8b5cf6', '#ec4899', '#6366f1', '#14b8a6', '#f97316', '#06b6d4'];

const dataTipos = computed(() => ({
    labels: props.stats.labels_tipo,
    datasets: [{ label: 'Cantidad', backgroundColor: backgroundColors, borderColor: '#ffffff', borderWidth: 2, data: props.stats.data_tipo }]
}));

const dataMedios = computed(() => ({
    labels: props.stats.labels_medio,
    datasets: [{ label: 'Cantidad', backgroundColor: backgroundColors.slice().reverse(), borderColor: '#ffffff', borderWidth: 2, data: props.stats.data_medio }]
}));

// --- Lógica del Formulario ---
const form = ref({
    mes_inicio: '',
    mes_fin: '',
    tipo_id: ''
});

const meses = [
    { id: 1, nombre: 'Enero' }, { id: 2, nombre: 'Febrero' }, { id: 3, nombre: 'Marzo' },
    { id: 4, nombre: 'Abril' }, { id: 5, nombre: 'Mayo' }, { id: 6, nombre: 'Junio' },
    { id: 7, nombre: 'Julio' }, { id: 8, nombre: 'Agosto' }, { id: 9, nombre: 'Septiembre' },
    { id: 10, nombre: 'Octubre' }, { id: 11, nombre: 'Noviembre' }, { id: 12, nombre: 'Diciembre' }
];

// --- Lógica del Modal / Iframe ---
const mostrarModal = ref(false);
const iframeRef = ref(null);
const iframeCargado = ref(false);

// --- SOLUCIÓN DEFINITIVA: Carga manual del SRC ---
const generarReporte = async () => {
    // 1. Validaciones
    if(!form.value.mes_inicio || !form.value.mes_fin) return alert("Seleccione el rango de meses");
    if(form.value.mes_fin < form.value.mes_inicio) return alert("El mes final no puede ser anterior al inicial");

    // 2. Construir la URL manualmente
    const baseUrl = '/infracciones/generar-reporte-pdf';
    const params = new URLSearchParams({
        mes_inicio: form.value.mes_inicio,
        mes_fin: form.value.mes_fin,
        tipo_id: form.value.tipo_id || '' // Si es vacío envía cadena vacía
    });
    const urlFinal = `${baseUrl}?${params.toString()}`;

    // 3. Mostrar modal y resetear estado
    iframeCargado.value = false;
    mostrarModal.value = true;

    // 4. Esperar a que el iframe exista en el DOM
    await nextTick();

    // 5. Asignar la URL directamente al iframe (Esto evita el redirect de la página principal)
    if (iframeRef.value) {
        iframeRef.value.src = urlFinal;
    }
};

const cerrarModal = () => {
    mostrarModal.value = false;
    iframeCargado.value = false;
    if (iframeRef.value) iframeRef.value.src = 'about:blank';
};

const onIframeLoad = () => {
    // Solo marcamos como cargado si el src no es about:blank
    if (iframeRef.value?.src !== 'about:blank') {
        setTimeout(() => { iframeCargado.value = true; }, 200);
    }
};

const imprimirIframe = () => {
    if (iframeRef.value && iframeRef.value.contentWindow) {
        iframeRef.value.contentWindow.print();
    }
};
</script>

<template>
    <PublicLayout title="Panel de Reportes">
        <div class="min-h-screen bg-gradient-to-br from-red-50 to-pink-100 dark:from-gray-900 dark:to-gray-800 py-8 px-4 sm:px-6 lg:px-8">

            <!-- Header -->
            <nav class="max-w-7xl mx-auto mb-8 flex items-center gap-3">
                <div class="w-10 h-10 bg-red-600 dark:bg-red-500 rounded-lg flex items-center justify-center shadow-lg text-white">
                    <i class="bi bi-pie-chart-fill text-xl"></i>
                </div>
                <div>
                    <h1 class="text-xl font-bold text-gray-900 dark:text-white">Panel Estadístico</h1>
                    <p class="text-xs text-gray-600 dark:text-gray-400">Resumen global de infracciones</p>
                </div>
            </nav>

            <div class="max-w-7xl mx-auto space-y-8">

                <!-- Gráficas -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div class="bg-white dark:bg-gray-800 p-6 rounded-2xl shadow-xl border border-gray-200 dark:border-gray-700 flex flex-col items-center">
                        <h3 class="text-lg font-bold text-gray-800 dark:text-white mb-4 w-full text-left">Infracciones más recurrentes</h3>
                        <div class="h-72 w-full max-w-xs"><Pie :data="dataTipos" :options="chartOptions" /></div>
                    </div>
                    <div class="bg-white dark:bg-gray-800 p-6 rounded-2xl shadow-xl border border-gray-200 dark:border-gray-700 flex flex-col items-center">
                        <h3 class="text-lg font-bold text-gray-800 dark:text-white mb-4 w-full text-left">Medios de Infracción</h3>
                        <div class="h-72 w-full max-w-xs"><Pie :data="dataMedios" :options="chartOptions" /></div>
                    </div>
                </div>

                <!-- Generador de Reportes -->
                <div class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl border border-gray-200 dark:border-gray-700 overflow-hidden">
                    <div class="p-6 border-b border-gray-100 dark:border-gray-700 bg-gradient-to-r from-white to-red-50 dark:from-gray-800 dark:to-gray-900/50 text-center">
                        <h2 class="text-xl font-bold text-gray-900 dark:text-white inline-flex items-center gap-2">
                            <i class="bi bi-file-earmark-pdf text-red-600"></i> Generar Reporte PDF
                        </h2>
                        <p class="text-sm text-gray-500 dark:text-gray-400 mt-1">Seleccione rango de fechas y tipo de infracción</p>
                    </div>

                    <div class="p-8">
                        <!-- FORMULARIO SIN ACTION NI TARGET -->
                        <form @submit.prevent="generarReporte">
                            <div class="grid grid-cols-1 md:grid-cols-4 gap-6 items-end">

                                <!-- Mes Inicio -->
                                <div class="space-y-2">
                                    <label class="block text-xs font-bold text-gray-500 uppercase">Desde</label>
                                    <select v-model="form.mes_inicio" required class="block w-full rounded-xl border-gray-300 dark:border-gray-600 dark:bg-gray-900/50 dark:text-white focus:border-red-500 focus:ring-red-500 py-2.5">
                                        <option value="" disabled>Mes inicial...</option>
                                        <option v-for="mes in meses" :key="mes.id" :value="mes.id">{{ mes.nombre }}</option>
                                    </select>
                                </div>

                                <!-- Mes Fin -->
                                <div class="space-y-2">
                                    <label class="block text-xs font-bold text-gray-500 uppercase">Hasta</label>
                                    <select v-model="form.mes_fin" required class="block w-full rounded-xl border-gray-300 dark:border-gray-600 dark:bg-gray-900/50 dark:text-white focus:border-red-500 focus:ring-red-500 py-2.5">
                                        <option value="" disabled>Mes final...</option>
                                        <option v-for="mes in meses" :key="mes.id" :value="mes.id" :disabled="form.mes_inicio && mes.id < form.mes_inicio">
                                            {{ mes.nombre }}
                                        </option>
                                    </select>
                                </div>

                                <!-- Tipo -->
                                <div class="space-y-2">
                                    <label class="block text-xs font-bold text-gray-500 uppercase">Tipo de Infracción</label>
                                    <select v-model="form.tipo_id" class="block w-full rounded-xl border-gray-300 dark:border-gray-600 dark:bg-gray-900/50 dark:text-white focus:border-red-500 focus:ring-red-500 py-2.5">
                                        <option value="">Todas las infracciones</option>
                                        <option v-for="tipo in tipos_infraccion" :key="tipo.id" :value="tipo.id">
                                            {{ tipo.nombre }}
                                        </option>
                                    </select>
                                </div>

                                <!-- Botón -->
                                <div>
                                    <button type="submit" class="w-full inline-flex items-center justify-center px-6 py-2.5 text-base font-bold text-white bg-red-600 hover:bg-red-700 dark:bg-red-500 dark:hover:bg-red-600 rounded-xl shadow-lg transition-all hover:-translate-y-0.5">
                                        <i class="bi bi-file-pdf-fill mr-2"></i> Generar
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- MODAL -->
            <div v-if="mostrarModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
                <div class="absolute inset-0 bg-gray-900/80 backdrop-blur-sm" @click="cerrarModal"></div>
                <div class="relative bg-white dark:bg-gray-800 rounded-2xl shadow-2xl flex flex-col overflow-hidden border border-gray-200 dark:border-gray-700 animate-fadeIn" style="width: 90vw; height: 90vh; max-width: 1400px;">
                    <div class="flex-none flex items-center justify-between px-6 py-4 border-b border-gray-200 dark:border-gray-700 bg-gray-50 dark:bg-gray-900">
                        <h3 class="text-lg font-bold text-gray-900 dark:text-white flex items-center gap-2">
                            <i class="bi bi-eye-fill text-red-600"></i> Vista Previa del Reporte
                        </h3>
                        <div class="flex items-center gap-3">
                            <button @click="imprimirIframe" :disabled="!iframeCargado" class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium text-sm flex items-center gap-2 shadow-sm disabled:opacity-50">
                                <i class="bi bi-printer-fill"></i> Imprimir
                            </button>
                            <button @click="cerrarModal" class="px-4 py-2 bg-white dark:bg-gray-700 border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-200 rounded-lg font-medium text-sm hover:bg-gray-50 transition-colors flex items-center gap-2">
                                <i class="bi bi-x-circle-fill"></i> Cerrar
                            </button>
                        </div>
                    </div>
                    <div class="flex-grow bg-gray-200 dark:bg-gray-900 overflow-auto relative">
                        <div v-if="!iframeCargado" class="absolute inset-0 flex items-center justify-center text-gray-400 z-20 bg-gray-100 dark:bg-gray-900">
                            <i class="bi bi-arrow-repeat animate-spin text-4xl"></i>
                        </div>
                        <!-- IFRAME SIN NAME NI TARGET -->
                        <iframe ref="iframeRef" class="absolute inset-0 w-full h-full bg-white" style="border: none;" @load="onIframeLoad"></iframe>
                    </div>
                </div>
            </div>

        </div>
    </PublicLayout>
</template>
