<script setup>
import { computed } from 'vue'
import { Head, Link, useForm, usePage } from '@inertiajs/vue3'
import PublicLayout from "@/Layouts/PublicLayout.vue"

defineProps({
    canResetPassword: Boolean,
    status: String,
})

const page = usePage()

const form = useForm({
    email: '',
    password: '',
    remember: false,
})

/**
 * ERROR REAL DE AUTENTICACIÓN
 * Fortify lo envía vía sesión → Inertia → page.props.errors
 */
const authError = computed(() => {
    return page.props.errors && Object.keys(page.props.errors).length > 0
})

const submit = () => {
    form.transform(data => ({
        ...data,
        remember: form.remember ? 'on' : '',
    }))
        .post(route('login'), {
            preserveScroll: true,
            onFinish: () => form.reset('password'),
        })
}
</script>

<template>
    <PublicLayout name="Iniciar Sesión">
        <Head title="Iniciar Sesión" />

        <div class="min-h-screen bg-gradient-to-br from-red-50 to-pink-100 dark:from-gray-900 dark:to-gray-800 flex flex-col justify-center py-12 px-4 sm:px-6 lg:px-8">

            <div class="sm:mx-auto sm:w-full sm:max-w-md">
                <Link href="/" class="flex justify-center">
                    <div class="w-16 h-16 bg-red-600 rounded-2xl flex items-center justify-center shadow-lg">
                        <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                        </svg>
                    </div>
                </Link>

                <h2 class="mt-6 text-center text-3xl font-bold text-gray-900 dark:text-white">
                    Iniciar Sesión
                </h2>

                <p class="mt-2 text-center text-sm text-gray-600 dark:text-gray-400">
                    Portal de Infracciones CDMX
                </p>
            </div>

            <div class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
                <div class="bg-white dark:bg-gray-800 py-8 px-6 shadow-xl rounded-2xl border border-gray-200 dark:border-gray-700">

                    <!-- ERROR REAL DE LOGIN -->
                    <div
                        v-if="authError"
                        class="mb-4 p-3 rounded-lg bg-red-50 border border-red-200"
                    >
                        <p class="text-sm font-medium text-red-600">
                            Las credenciales proporcionadas no son válidas o el usuario no existe.
                        </p>
                    </div>

                    <form @submit.prevent="submit" class="space-y-6">

                        <div>
                            <label class="block text-sm font-medium text-gray-700">
                                Correo electrónico
                            </label>
                            <input
                                v-model="form.email"
                                type="email"
                                required
                                autofocus
                                class="mt-1 block w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-red-500"
                            />
                        </div>

                        <div>
                            <label class="block text-sm font-medium text-gray-700">
                                Contraseña
                            </label>
                            <input
                                v-model="form.password"
                                type="password"
                                required
                                class="mt-1 block w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-red-500"
                            />
                        </div>

                        <button
                            type="submit"
                            :disabled="form.processing"
                            class="w-full py-3 text-white bg-red-600 hover:bg-red-700 rounded-lg shadow"
                        >
                            Iniciar sesión
                        </button>
                    </form>
                </div>

                <div class="mt-6 text-center">
                    <Link href="/" class="text-sm text-gray-600 hover:text-red-600">
                        ← Volver al inicio
                    </Link>
                </div>
            </div>
        </div>
    </PublicLayout>
</template>
