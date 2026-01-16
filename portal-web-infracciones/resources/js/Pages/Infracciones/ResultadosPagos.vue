<script setup>
import { ref } from 'vue';
import { Link, useForm } from '@inertiajs/vue3';
import PublicLayout from "@/Layouts/PublicLayout.vue";

const props = defineProps({
    folio_consultado: String,
    lista_pagos: Array,
    infraccion: Object,
    strapi_url: String,
    error: String,
});

// --- Lógica para el Modal y Mensajes ---
const showModal = ref(false);
const mensajeExito = ref(false); // Nuevo estado para el mensaje de éxito
const pagoSeleccionado = ref(null);

const formReimpresion = useForm({
    linea_captura: '',
    folio_infraccion: '',
    fecha_pago: '',
    monto_display: ''
});

const abrirModalReimpresion = (pago) => {
    mensajeExito.value = false; // Reseteamos el mensaje al intentar de nuevo
    pagoSeleccionado.value = pago;

    formReimpresion.linea_captura = pago.linea_captura;
    formReimpresion.folio_infraccion = props.folio_consultado || props.infraccion?.folio;
    formReimpresion.fecha_pago = pago.fecha_pago || new Date().toISOString();
    formReimpresion.monto_display = pago.monto;

    showModal.value = true;
};

const cerrarModal = () => {
    showModal.value = false;
    formReimpresion.reset();
    pagoSeleccionado.value = null;
};

const confirmarReimpresion = () => {
    formReimpresion.post(route('pagos.solicitar-reimpresion'), {
        preserveScroll: true,
        onSuccess: () => {
            cerrarModal();
            // Activamos el mensaje de éxito en lugar del alert
            mensajeExito.value = true;

            // Opcional: Ocultar el mensaje automáticamente después de 10 segundos
            setTimeout(() => {
                mensajeExito.value = false;
            }, 10000);
        },
        onError: (errors) => {
            if (errors.error) {
                alert(errors.error);
            } else {
                alert('Hubo un error al procesar la solicitud.');
            }
        }
    });
};

// --- Helpers de Formato ---
const formatearFecha = (fechaIso) => {
    if (!fechaIso) return 'Sin fecha';
    return new Date(fechaIso).toLocaleDateString('es-MX', {
        year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'
    });
};

const formatearMonto = (monto) => {
    if (!monto) return '$0.00';
    return new Intl.NumberFormat('es-MX', {
        style: 'currency',
        currency: 'MXN'
    }).format(monto);
};

const getEstadoBadgeClass = (estatus) => {
    const estatusLower = estatus?.toLowerCase() || '';

    if (estatusLower.includes('pagado') || estatusLower.includes('completado')) {
        return 'bg-green-100 dark:bg-green-900 text-green-800 dark:text-green-200';
    }
    if (estatusLower.includes('pendiente')) {
        return 'bg-yellow-100 dark:bg-yellow-900 text-yellow-800 dark:text-yellow-200';
    }
    if (estatusLower.includes('vencido')) {
        return 'bg-orange-100 dark:bg-orange-900 text-orange-800 dark:text-orange-200';
    }
    if (estatusLower.includes('cancelado') || estatusLower.includes('rechazado')) {
        return 'bg-red-100 dark:bg-red-900 text-red-800 dark:text-red-200';
    }
    return 'bg-gray-100 dark:bg-gray-700 text-gray-800 dark:text-gray-200';
};
</script>

<template>
    <PublicLayout title="Resultados de Pagos">
        <div class="min-h-screen bg-gradient-to-br from-red-50 to-pink-100 dark:from-gray-900 dark:to-gray-800 py-8 px-4 sm:px-6 lg:px-8 relative">
            <div class="max-w-6xl mx-auto">

                <!-- Header y Botón Nueva Consulta -->
                <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-8 gap-4">
                    <div>
                        <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-2">
                            Resultados de Pagos
                        </h1>
                        <div class="flex items-center gap-2">
                            <span class="text-gray-600 dark:text-gray-400">Folio:</span>
                            <span class="inline-flex items-center px-3 py-1 rounded-full text-sm font-semibold bg-red-100 dark:bg-red-900 text-red-800 dark:text-red-200">
                                {{ folio_consultado }}
                            </span>
                        </div>
                    </div>
                    <Link
                        :href="route('pagos.index')"
                        class="inline-flex items-center px-4 py-2 bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-600 rounded-lg text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 dark:focus:ring-offset-gray-900 transition duration-200 shadow-sm"
                    >
                        <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
                        </svg>
                        Nueva Consulta
                    </Link>
                </div>

                <!-- MENSAJE DE ÉXITO (NUEVO) -->
                <div v-if="mensajeExito" class="bg-green-50 dark:bg-green-900/30 border-l-4 border-green-500 p-4 rounded-r-lg mb-6 shadow-md transition-all duration-500 ease-in-out">
                    <div class="flex">
                        <div class="flex-shrink-0">
                            <svg class="h-5 w-5 text-green-400" viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                            </svg>
                        </div>
                        <div class="ml-3">
                            <h3 class="text-sm leading-5 font-medium text-green-800 dark:text-green-200">
                                Solicitud enviada exitosamente
                            </h3>
                            <div class="mt-2 text-sm leading-5 text-green-700 dark:text-green-300">
                                <p>
                                    Tu solicitud de reimpresión ha sido recibida. Podrás ver reflejada tu nueva línea de captura en un lapso de <strong>24 a 48 horas</strong>.
                                </p>
                            </div>
                        </div>
                        <div class="ml-auto pl-3">
                            <div class="-mx-1.5 -my-1.5">
                                <button @click="mensajeExito = false" class="inline-flex rounded-md p-1.5 text-green-500 hover:bg-green-100 dark:hover:bg-green-800 focus:outline-none focus:bg-green-100 transition ease-in-out duration-150">
                                    <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                                        <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
                                    </svg>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Mensaje de Error -->
                <div v-if="error" class="bg-red-50 dark:bg-red-900/20 border-l-4 border-red-500 dark:border-red-400 p-4 rounded-r-lg mb-6">
                    <div class="flex items-center">
                        <svg class="w-6 h-6 text-red-500 dark:text-red-400 mr-3" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd"></path>
                        </svg>
                        <p class="text-sm font-medium text-red-800 dark:text-red-200">{{ error }}</p>
                    </div>
                </div>

                <!-- Infracción no encontrada -->
                <div v-if="!infraccion && !error" class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-12 text-center border border-gray-200 dark:border-gray-700">
                    <div class="inline-flex items-center justify-center w-20 h-20 bg-yellow-100 dark:bg-yellow-900 rounded-full mb-6">
                        <svg class="w-10 h-10 text-yellow-600 dark:text-yellow-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
                        </svg>
                    </div>
                    <h3 class="text-2xl font-bold text-gray-900 dark:text-white mb-2">
                        Folio No Encontrado
                    </h3>
                    <p class="text-gray-600 dark:text-gray-400 max-w-md mx-auto">
                        No se encontró ninguna infracción con el folio consultado. Verifica que el folio sea correcto.
                    </p>
                </div>

                <!-- Información de la Infracción -->
                <div v-if="infraccion" class="space-y-6">
                    <div class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-6 border border-gray-200 dark:border-gray-700">
                        <div class="flex items-center gap-3 mb-4">
                            <div class="inline-flex items-center justify-center w-10 h-10 bg-red-100 dark:bg-red-900 rounded-lg">
                                <svg class="w-6 h-6 text-red-600 dark:text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                                </svg>
                            </div>
                            <h2 class="text-xl font-bold text-gray-900 dark:text-white">Información de la Infracción</h2>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                            <div class="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-4">
                                <p class="text-xs text-gray-500 dark:text-gray-400 uppercase font-semibold mb-1">Placa</p>
                                <p class="text-lg font-bold text-gray-900 dark:text-white">{{ infraccion.placa_vehiculo || 'N/A' }}</p>
                            </div>
                            <div class="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-4">
                                <p class="text-xs text-gray-500 dark:text-gray-400 uppercase font-semibold mb-1">Tipo</p>
                                <p class="text-lg font-bold text-gray-900 dark:text-white">{{ infraccion.tipo_infraccion_id?.nombre || 'N/A' }}</p>
                            </div>
                            <div class="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-4">
                                <p class="text-xs text-gray-500 dark:text-gray-400 uppercase font-semibold mb-1">Fecha</p>
                                <p class="text-sm font-bold text-gray-900 dark:text-white">{{ formatearFecha(infraccion.fecha_infraccion) }}</p>
                            </div>
                        </div>
                    </div>

                    <!-- Sin Pagos -->
                    <div v-if="!lista_pagos || lista_pagos.length === 0" class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-12 text-center border border-gray-200 dark:border-gray-700">
                        <div class="inline-flex items-center justify-center w-20 h-20 bg-blue-100 dark:bg-blue-900 rounded-full mb-6">
                            <svg class="w-10 h-10 text-blue-600 dark:text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"></path>
                            </svg>
                        </div>
                        <h3 class="text-2xl font-bold text-gray-900 dark:text-white mb-2">
                            Sin Pagos Registrados
                        </h3>
                        <p class="text-gray-600 dark:text-gray-400 max-w-md mx-auto">
                            No se encontraron pagos asociados a esta infracción. La multa podría estar pendiente de pago.
                        </p>
                    </div>

                    <!-- Lista de Pagos -->
                    <div v-else class="space-y-6">
                        <div class="bg-white dark:bg-gray-800 rounded-lg shadow-sm p-4 border border-gray-200 dark:border-gray-700">
                            <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                                <div class="flex items-center gap-2">
                                    <span class="text-sm font-medium text-gray-700 dark:text-gray-300">
                                        Total de pagos encontrados:
                                    </span>
                                    <span class="inline-flex items-center px-3 py-1 rounded-full text-sm font-bold bg-red-100 dark:bg-red-900 text-red-800 dark:text-red-200">
                                        {{ lista_pagos.length }}
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div
                            v-for="pago in lista_pagos"
                            :key="pago.id"
                            class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl overflow-hidden border border-gray-200 dark:border-gray-700 hover:shadow-2xl transition-shadow duration-300"
                        >
                            <div class="p-6 space-y-4">
                                <div class="flex flex-col sm:flex-row sm:justify-between sm:items-start gap-4">
                                    <div class="flex-1">
                                        <div class="flex items-center gap-3 mb-3">
                                            <div class="inline-flex items-center justify-center w-10 h-10 bg-red-100 dark:bg-red-900 rounded-lg">
                                                <svg class="w-6 h-6 text-red-600 dark:text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"></path>
                                                </svg>
                                            </div>
                                            <div>
                                                <p class="text-xs text-gray-500 dark:text-gray-400 uppercase font-semibold">Línea de Captura</p>
                                                <p class="text-lg font-bold text-gray-900 dark:text-white font-mono">
                                                    {{ pago.linea_captura || 'No disponible' }}
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="flex flex-col items-start sm:items-end gap-2">
                                        <span :class="['inline-flex items-center px-3 py-1 rounded-full text-xs font-bold uppercase', getEstadoBadgeClass(pago.estatus)]">
                                            {{ pago.estatus || 'Sin estatus' }}
                                        </span>
                                        <div class="text-left sm:text-right">
                                            <p class="text-2xl font-bold text-gray-900 dark:text-white">
                                                {{ formatearMonto(pago.monto) }}
                                            </p>
                                        </div>

                                        <!-- BOTÓN SOLICITAR REIMPRESIÓN -->
                                        <button
                                            v-if="pago.estatus && pago.estatus.toLowerCase().includes('vencido')"
                                            @click="abrirModalReimpresion(pago)"
                                            class="mt-2 inline-flex items-center px-4 py-2 bg-orange-600 hover:bg-orange-700 text-white text-sm font-medium rounded-lg transition-colors duration-200 shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500 gap-2"
                                        >
                                            <i class="bi bi-file-earmark-check"></i>
                                            Solicitar Nueva Línea de Pago
                                        </button>
                                    </div>
                                </div>

                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <div class="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-4">
                                        <div class="flex items-start gap-2">
                                            <svg class="w-5 h-5 text-gray-500 dark:text-gray-400 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                            </svg>
                                            <div class="flex-1">
                                                <p class="text-xs text-gray-500 dark:text-gray-400 uppercase font-semibold mb-1">Vigencia</p>
                                                <p class="text-sm font-medium text-gray-900 dark:text-white">
                                                    {{ formatearFecha(pago.vigencia) }}
                                                </p>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-4">
                                        <div class="flex items-start gap-2">
                                            <svg class="w-5 h-5 text-gray-500 dark:text-gray-400 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                                            </svg>
                                            <div class="flex-1">
                                                <p class="text-xs text-gray-500 dark:text-gray-400 uppercase font-semibold mb-1">Fecha de Pago</p>
                                                <p class="text-sm font-medium text-gray-900 dark:text-white">
                                                    {{ formatearFecha(pago.fecha_pago) }}
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- MODAL DE CONFIRMACIÓN -->
            <div v-if="showModal" class="fixed inset-0 z-50 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
                <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
                    <!-- Background overlay -->
                    <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="cerrarModal"></div>

                    <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

                    <div class="inline-block align-bottom bg-white dark:bg-gray-800 rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
                        <div class="bg-white dark:bg-gray-800 px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
                            <div class="sm:flex sm:items-start">
                                <div class="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-orange-100 dark:bg-orange-900 sm:mx-0 sm:h-10 sm:w-10">
                                    <svg class="h-6 w-6 text-orange-600 dark:text-orange-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                                    </svg>
                                </div>
                                <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left w-full">
                                    <h3 class="text-lg leading-6 font-medium text-gray-900 dark:text-white" id="modal-title">
                                        Confirmar Solicitud de Reimpresión
                                    </h3>
                                    <div class="mt-4 space-y-3">
                                        <p class="text-sm text-gray-500 dark:text-gray-400">
                                            ¿Estás seguro que deseas solicitar la reimpresión para este pago vencido?
                                        </p>

                                        <div class="bg-gray-50 dark:bg-gray-700 rounded-md p-3 text-sm">
                                            <div class="grid grid-cols-2 gap-2">
                                                <span class="font-semibold text-gray-700 dark:text-gray-300">Folio Infracción:</span>
                                                <span class="text-gray-900 dark:text-white text-right">{{ formReimpresion.folio_infraccion }}</span>

                                                <span class="font-semibold text-gray-700 dark:text-gray-300">Monto:</span>
                                                <span class="text-gray-900 dark:text-white text-right">{{ formatearMonto(formReimpresion.monto_display) }}</span>

                                                <span class="font-semibold text-gray-700 dark:text-gray-300">Fecha Pago:</span>
                                                <span class="text-gray-900 dark:text-white text-right">{{ formatearFecha(formReimpresion.fecha_pago) }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="bg-gray-50 dark:bg-gray-700/50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
                            <button
                                type="button"
                                @click="confirmarReimpresion"
                                :disabled="formReimpresion.processing"
                                class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-orange-600 text-base font-medium text-white hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                <span v-if="formReimpresion.processing">Enviando...</span>
                                <span v-else>Confirmar Solicitud</span>
                            </button>
                            <button
                                type="button"
                                @click="cerrarModal"
                                class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 dark:border-gray-600 shadow-sm px-4 py-2 bg-white dark:bg-gray-800 text-base font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
                            >
                                Cancelar
                            </button>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </PublicLayout>
</template>
