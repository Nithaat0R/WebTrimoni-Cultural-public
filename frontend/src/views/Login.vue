<template>
  <div class="auth-wrapper">
    <div class="auth-card">
      <h2>Iniciar sessió</h2>

      <input
        type="email"
        v-model="email"
        placeholder="Correu electrònic"
      />

      <input
        type="password"
        v-model="password"
        placeholder="Contrasenya"
      />

      <button @click="login">Entrar</button>

      <p class="link">
        No tens compte?
        <router-link to="/register">Registra't</router-link>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      email: "",
      password: "",
    };
  },
  methods: {
    login() {
      console.log("LOGIN:", this.email, this.password);
      // aquí anirà Firebase / backend
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
</style>
