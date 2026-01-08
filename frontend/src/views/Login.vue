<template>
  <div class="auth-wrapper">
    <div class="auth-card">
      <h2>Iniciar sessió</h2>

      <input type="email" v-model="email" placeholder="Correu electrònic" />

      <input type="password" v-model="password" placeholder="Contrasenya" />

      <button @click="login">Entrar</button>

      <p v-if="error" class="error">{{ error }}</p>

      <p class="link">
        No tens compte?
        <router-link to="/register">Registra't</router-link>
      </p>
    </div>
  </div>
</template>

<script>
import { currentUser } from "@/store/userStore"; // tu store global
import { auth } from "@/firebase";
import { signInWithEmailAndPassword } from "firebase/auth";

export default {
  data() {
    return {
      email: "",
      password: "",
      error: null
    };
  },
  methods: {
    async login() {
      this.error = null;

      try {
        // 1️⃣ Login con Firebase
        const userCredential = await signInWithEmailAndPassword(
          auth,
          this.email,
          this.password
        );

        const user = userCredential.user;

        // 2️⃣ Guardar usuario en estado global
        currentUser.value = user;
        console.log(user)

        // 3️⃣ Redirigir al inicio
        this.$router.push("/");

      } catch (err) {
        this.error = "Correu o contrasenya incorrectes";
        console.error(err);
      }
    }
  }
};
</script>

<style scoped>
.auth-wrapper {
  min-height: 100vh;
  background: #ffe3e3;
  display: flex;
  align-items: center;
  justify-content: center;
}

.auth-card {
  background: #ff8f8f;
  padding: 30px;
  border-radius: 16px;
  width: 320px;
  text-align: center;
}

.auth-card h2 {
  margin-bottom: 20px;
  color: white;
}

.auth-card input {
  width: 100%;
  padding: 10px;
  margin-bottom: 12px;
  border-radius: 8px;
  border: none;
  font-size: 14px;
}

.auth-card button {
  width: 100%;
  padding: 10px;
  background: #e04545;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
}

.auth-card button:hover {
  background: #c83b3b;
}

.link {
  margin-top: 16px;
  font-size: 14px;
}

.link a {
  color: white;
  font-weight: bold;
}

.error {
  margin-top: 10px;
  color: white;
  font-size: 14px;
}
</style>
