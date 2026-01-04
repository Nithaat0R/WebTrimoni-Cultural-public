import { ref } from "vue";
import { auth } from "@/firebase"; // tu instancia de Firebase

// Usuario actual, null si no está logueado
export const currentUser = ref(null);

// Función para inicializar y mantener el estado
export function initAuth() {
    // Se llama una vez al arrancar la app
    auth.onAuthStateChanged(user => {
        if (user) {
            currentUser.value = user; // Objeto Firebase Auth
        } else {
            currentUser.value = null;
        }
    });
}
