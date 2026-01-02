<template>
  <div class="auth-wrapper">
    <div class="card">
      <h2>Registre</h2>

      <input
        v-model="username"
        type=""
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

      <p class="link" @click="$router.push('/login')">
        Ja tens compte? Inicia sessió
      </p>
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

        // tras registro → login
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
  display: flex;
  justify-content: center;
  align-items: center;
  background: #ffe5e5;
}

.card {
  background: #fff;
  padding: 30px;
  width: 320px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0,0,0,.1);
}

input {
  width: 100%;
  padding: 10px;
  margin-bottom: 12px;
  border-radius: 6px;
  border: 1px solid #ccc;
}

button {
  width: 100%;
  padding: 10px;
  background: #c62828;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

button:hover {
  background: #b71c1c;
}

.error {
  color: red;
  margin-top: 10px;
}

.link {
  margin-top: 15px;
  color: #c62828;
  cursor: pointer;
  text-align: center;
}
</style>
