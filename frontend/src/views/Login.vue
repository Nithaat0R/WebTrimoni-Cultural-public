<template>
  <div class="auth-wrapper">
    <div class="auth-container">
      <img src="@/assets/LOGO PATRIMONI.png" alt="Logo de la web" class="auth-logo" />

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
  </div>
</template>

<script>
import { currentUser } from "@/store/userStore";
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
        // Login con Firebase
        const userCredential = await signInWithEmailAndPassword(
          auth,
          this.email,
          this.password
        );

        const user = userCredential.user;

        // Guardar usuario en estado global
        currentUser.value = user;
        console.log(user)

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
  margin: 4%;
  align-items: top;
  justify-content: center;
}

.auth-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.auth-logo {
  max-width: 120px;
  height: auto;
}

.auth-card {
  background: #ff8f8f;
  padding: 30px;
  border-radius: 16px;
  width: 320px;
  text-align: center;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.auth-card h2 {
  margin-bottom: 20px;
  color: #d42828;
}

.auth-card input {
  width: 100%;
  padding: 10px;
  margin-bottom: 12px;
  border-radius: 8px;
  border: none;
  font-size: 14px;
  box-sizing: border-box;
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
  color: #d42828;
  font-weight: bold;
}

.error {
  margin-top: 10px;
  color: white;
  font-size: 14px;
}
</style>