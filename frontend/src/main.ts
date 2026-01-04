import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import { initAuth } from "@/store/userStore"; // la ruta según donde lo hayas puesto

const app = createApp(App)

// Inicializar Firebase Auth para mantener el estado del usuario
initAuth();

app.use(router)
app.mount('#app')
