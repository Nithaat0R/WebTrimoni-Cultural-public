<template>
  <div class="auth-wrapper">
    <div class="auth-container">
      <img src="@/assets/LOGO PATRIMONI.png" alt="Logo de la web" class="auth-logo" />

      <div class="auth-card">
        <h2>Registre</h2>

        <input
          v-model="username"
          type="text"
          placeholder="Nom d'usuari"
        />

        <input
          v-model="email"
          type="email"
          placeholder="Email"
        />

        <input
          v-model="password"
          type="password"
          placeholder="Contrasenya"
        />

        <button @click="register">Crear compte</button>

        <p v-if="error" class="error">{{ error }}</p>

        <p class="link">
          Ja tens compte? 
          <router-link to="/login">Inicia sessió</router-link>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Register",
  data() {
    return {
      username: "",
      email: "",
      password: "",
      error: "",
    };
  },
  methods: {
    async register() {
      this.error = "";
      try {
        await axios.post(
          "http://localhost:8080/api/auth/register",
          {
            username: this.username,
            email: this.email,
            password: this.password,
          }
        );

        this.$router.push("/login");
      } catch (e) {
        this.error = "No s'ha pogut crear el compte";
      }
    },
  },
};
</script>

<style scoped>
.auth-wrapper {
  min-height: 100vh;
  background: #ffe3e3;
  display: flex;
  margin: 4%;
  align-items: flex-start;
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
  text-decoration: none;
}

.error {
  margin-top: 10px;
  color: white;
  font-size: 14px;
}
</style>