<template>
  <div class="perfil-wrapper">
    <!-- CABECERA PERFIL -->
    <div class="perfil-card">
      <div class="avatar">
        <div class="avatar-icon">👤</div>
      </div>

      <div class="perfil-info">
        <div class="info-row">
          <strong>{{ usuari.nom }}</strong>
          <button class="edit-btn">✎</button>
        </div>

        <div class="info-row">
          <span>{{ usuari.email }}</span>
          <button class="edit-btn">✎</button>
        </div>

        <div class="info-row">
          <span class="password">************</span>
          <button class="icon-btn">👁</button>
          <button class="edit-btn">✎</button>
        </div>
      </div>
    </div>

    <!-- PATRIMONIS PREFERITS -->
    <h2 class="section-title">PATRIMONIS PREFERITS</h2>

    <div class="favorits">
      <div
        class="favorit-card"
        v-for="(p, i) in patrimonis"
        :key="i"
      >
        <div class="favorit-img">Imatge</div>
        <div class="favorit-title">{{ p }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ZonaPersonal",
  data() {
    return {
      usuari: {
        nom: "Nom Cognom1 Cognom2",
        email: "mail@mail.com",
      },
      patrimonis: [
        "Sant Pere d'Abrera",
        "Monestir de Ripoll",
        "Castell de Cardona"
      ]
    };
  }
};
</script>

<style scoped>
.perfil-wrapper {
  padding: 24px;
  background: #ffe3e3;
  min-height: 100vh;
}

/* PERFIL */
.perfil-card {
  display: flex;
  gap: 24px;
  background: #ff8f8f;
  border-radius: 14px;
  padding: 24px;
  margin-bottom: 30px;
}

.avatar {
  width: 160px;
  height: 160px;
  background: #e0e0e0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  font-size: 64px;
  color: #c44;
}

.perfil-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 18px;
}

.password {
  letter-spacing: 3px;
}

.edit-btn,
.icon-btn {
  background: #ff5c5c;
  border: none;
  color: white;
  border-radius: 6px;
  padding: 4px 8px;
  cursor: pointer;
}

.edit-btn:hover,
.icon-btn:hover {
  background: #e04545;
}

/* FAVORITS */
.section-title {
  color: #e04545;
  font-weight: bold;
  margin-bottom: 16px;
}

.favorits {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
}

.favorit-card {
  background: #ff9f9f;
  border-radius: 12px;
  padding: 16px;
  text-align: center;
}

.favorit-img {
  background: #fff;
  height: 120px;
  border-radius: 8px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
}

.favorit-title {
  font-weight: bold;
}
</style>
