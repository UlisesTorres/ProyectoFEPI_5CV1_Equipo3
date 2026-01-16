<script setup>
import { ref } from 'vue';
import PublicLayout from "@/Layouts/PublicLayout.vue";

const form = ref({
    nombre_solicitante: '',
    placa_vehiculo: '',
    folio_infraccion: '',
    observaciones: ''
});

const mostrarModal = ref(false);
const iframeRef = ref(null);
const iframeCargado = ref(false);

const generarVista = () => {
    iframeCargado.value = false;
    mostrarModal.value = true;
};

const cerrarModal = () => {
    mostrarModal.value = false;
    iframeCargado.value = false;
    if (iframeRef.value) iframeRef.value.src = 'about:blank';
};

const onIframeLoad = () => {
    setTimeout(() => {
        iframeCargado.value = true;
    }, 100);
};

const imprimirIframe = () => {
    if (iframeRef.value && iframeRef.value.contentWindow) {
        iframeRef.value.contentWindow.print();
    }
};
</script>

<template>
    <PublicLayout title="Formato de Apelacion de Infracciones">
        <div class="min-h-screen bg-gradient-to-br from-red-50 to-pink-100 dark:from-gray-900 dark:to-gray-800 py-8 px-4 sm:px-6 lg:px-8">

            <!-- Barra de navegación -->
            <nav class="max-w-2xl mx-auto mb-8">
                <div class="flex justify-between items-center">
                    <div class="flex items-center gap-3">
                        <div class="w-10 h-10 bg-red-600 dark:bg-red-500 rounded-lg flex items-center justify-center shadow-lg text-white">
                            <i class="bi bi-file-earmark-text-fill text-xl"></i>
                        </div>
                        <div>
                            <h1 class="text-xl font-bold text-gray-900 dark:text-white">Portal de Infracciones</h1>
                            <p class="text-xs text-gray-600 dark:text-gray-400">Solicitud de Apelación</p>
                        </div>
                    </div>

                </div>
            </nav>

            <!-- Formulario Principal -->
            <div class="max-w-2xl mx-auto">
                <div class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl border border-gray-200 dark:border-gray-700 overflow-hidden">
                    <div class="p-8 border-b border-gray-100 dark:border-gray-700 bg-gradient-to-r from-white to-red-50 dark:from-gray-800 dark:to-gray-900/50">
                        <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-2">Solicitud de Apelación</h2>
                        <p class="text-gray-600 dark:text-gray-400 text-sm">Complete los datos para generar su formato de apelación.</p>
                    </div>

                    <div class="p-8">
                        <form action="/infracciones/generar-vista" method="GET" target="vista-previa-frame" @submit="generarVista">

                            <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
                                <!-- Nombre del Solicitante -->
                                <div class="space-y-3 md:col-span-2">
                                    <label class="block font-medium text-sm text-gray-700 dark:text-gray-300">Nombre del Solicitante</label>
                                    <div class="relative">
                                        <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                                            <i class="bi bi-person-fill text-xl text-gray-400"></i>
                                        </div>
                                        <input type="text" name="nombre_solicitante" v-model="form.nombre_solicitante" required
                                               class="!pl-14 block w-full rounded-xl border-gray-300 dark:border-gray-600 dark:bg-gray-900/50 dark:text-white shadow-sm focus:border-red-500 focus:ring-red-500 py-3"
                                               style="padding-left: 3.5rem;" placeholder="Nombre completo">
                                    </div>
                                </div>

                                <!-- Placa -->
                                <div class="space-y-3">
                                    <label class="block font-medium text-sm text-gray-700 dark:text-gray-300">Placa del Vehículo</label>
                                    <div class="relative">
                                        <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                                            <i class="bi bi-car-front-fill text-xl text-gray-400"></i>
                                        </div>
                                        <input type="text" name="placa_vehiculo" v-model="form.placa_vehiculo" required
                                               class="!pl-14 block w-full rounded-xl border-gray-300 dark:border-gray-600 dark:bg-gray-900/50 dark:text-white shadow-sm focus:border-red-500 focus:ring-red-500 uppercase py-3"
                                               style="padding-left: 3.5rem;" placeholder="Ej. ABC-123">
                                    </div>
                                </div>

                                <!-- Folio de Infracción -->
                                <div class="space-y-3">
                                    <label class="block font-medium text-sm text-gray-700 dark:text-gray-300">Folio de Infracción</label>
                                    <div class="relative">
                                        <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                                            <i class="bi bi-hash text-xl text-gray-400"></i>
                                        </div>
                                        <input type="text" name="folio_infraccion" v-model="form.folio_infraccion" required
                                               class="!pl-14 block w-full rounded-xl border-gray-300 dark:border-gray-600 dark:bg-gray-900/50 dark:text-white shadow-sm focus:border-red-500 focus:ring-red-500 uppercase py-3"
                                               style="padding-left: 3.5rem;" placeholder="Ej. INF-2024-001234">
                                    </div>
                                </div>
                            </div>

                            <!-- Observaciones -->
                            <div class="mt-8 space-y-3">
                                <label class="block font-medium text-sm text-gray-700 dark:text-gray-300">Motivo de la Apelación</label>
                                <div class="relative">
                                    <div class="absolute top-4 left-0 pl-4 pointer-events-none">
                                        <i class="bi bi-chat-text-fill text-xl text-gray-400"></i>
                                    </div>
                                    <textarea name="observaciones" v-model="form.observaciones" rows="4"
                                              class="!pl-14 block w-full rounded-xl border-gray-300 dark:border-gray-600 dark:bg-gray-900/50 dark:text-white shadow-sm focus:border-red-500 focus:ring-red-500 py-3"
                                              style="padding-left: 3.5rem;" placeholder="Describa el motivo por el cual solicita la apelación..."></textarea>
                                </div>
                            </div>

                            <!-- Botones -->
                            <div class="mt-10 pt-8 border-t border-gray-100 dark:border-gray-700 flex flex-col sm:flex-row items-center justify-end gap-4">
                                <button type="reset" class="w-full sm:w-auto px-6 py-3 text-base font-medium text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white transition-colors flex items-center justify-center gap-2 rounded-xl hover:bg-gray-100 dark:hover:bg-gray-700">
                                    <i class="bi bi-eraser-fill"></i> Limpiar
                                </button>

                                <button type="submit" class="w-full sm:w-auto inline-flex items-center justify-center px-8 py-4 text-lg font-bold text-white bg-red-600 hover:bg-red-700 dark:bg-red-500 dark:hover:bg-red-600 rounded-xl shadow-lg hover:shadow-xl hover:-translate-y-0.5 transition-all duration-200">
                                    <i class="bi bi-eye-fill mr-3"></i> Previsualizar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- MODAL CON IFRAME -->
            <div v-if="mostrarModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
                <div class="absolute inset-0 bg-gray-900/80 backdrop-blur-sm transition-opacity" @click="cerrarModal"></div>

                <div
                    class="relative bg-white dark:bg-gray-800 rounded-2xl shadow-2xl flex flex-col overflow-hidden border border-gray-200 dark:border-gray-700 animate-fadeIn"
                    style="width: 90vw; height: 90vh; max-width: 1400px;"
                >

                    <!-- Header -->
                    <div class="flex-none flex items-center justify-between px-6 py-4 border-b border-gray-200 dark:border-gray-700 bg-gray-50 dark:bg-gray-900">
                        <h3 class="text-lg font-bold text-gray-900 dark:text-white flex items-center gap-2">
                            <i class="bi bi-file-earmark-pdf-fill text-red-600"></i> Vista Previa
                        </h3>
                        <div class="flex items-center gap-3">
                            <button @click="imprimirIframe" :disabled="!iframeCargado" class="px-4 py-2 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 disabled:cursor-not-allowed text-white rounded-lg font-medium text-sm flex items-center gap-2 transition-colors shadow-sm">
                                <i class="bi bi-printer-fill"></i> Imprimir
                            </button>
                            <button @click="cerrarModal" class="px-4 py-2 bg-white dark:bg-gray-700 border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-200 rounded-lg font-medium text-sm hover:bg-gray-50 dark:hover:bg-gray-600 transition-colors flex items-center gap-2">
                                <i class="bi bi-x-circle-fill"></i> Cerrar
                            </button>
                        </div>
                    </div>

                    <!-- Cuerpo del Modal (Iframe) -->
                    <div class="flex-grow bg-gray-200 dark:bg-gray-900 overflow-auto relative" style="min-height: 0;">
                        <div v-if="!iframeCargado" class="absolute inset-0 flex items-center justify-center text-gray-400 z-20 bg-gray-100 dark:bg-gray-900">
                            <i class="bi bi-arrow-repeat animate-spin text-4xl"></i>
                        </div>

                        <iframe
                            ref="iframeRef"
                            name="vista-previa-frame"
                            class="absolute inset-0 w-full h-full bg-white"
                            style="border: none; min-height: 100%;"
                            @load="onIframeLoad"
                        ></iframe>
                    </div>
                </div>
            </div>
        </div>
    </PublicLayout>
</template>

<style scoped>
@keyframes fadeIn {
    from { opacity: 0; transform: scale(0.95); }
    to { opacity: 1; transform: scale(1); }
}
.animate-fadeIn {
    animation: fadeIn 0.2s ease-out forwards;
}

@keyframes spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
}
.animate-spin {
    animation: spin 1s linear infinite;
}
</style>
