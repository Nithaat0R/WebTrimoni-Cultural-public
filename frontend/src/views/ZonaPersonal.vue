<template>
  <div class="perfil-container">
    <header class="perfil-header">
      <div class="header-content">
        <img src="@/assets/LOGO PATRIMONI.png" alt="WebTrimoni Logo" class="logo" />
        <h1>El teu Perfil</h1>
      </div>
    </header>

    <div class="perfil-content">
      <div v-if="cargando" class="loading-state">
        <p>Carregant dades de l'usuari…</p>
      </div>

      <div v-else>
        <div class="user-main-card">
          <div class="user-header-info">
            <div class="avatar-circle">👤</div>
            <div class="user-text">
              <h2>{{ usuari.nom }}</h2>
              <p class="email-text">📧 {{ usuari.email }}</p>
            </div>
          </div>

          <div class="user-actions">
            <div class="user-stats-row">
              <div class="mini-stat">
                <strong>{{ numComentaris }}</strong> <span>Comentaris</span>
              </div>
              <div class="mini-stat">
                <strong>{{ favorits.length }}</strong> <span>Preferits</span>
              </div>
            </div>
            
            <div class="buttons-group">
              <button @click="restablirContrasenya" class="btn-secondary">
                Restablir contrasenya ⚙
              </button>
              <button class="btn-logout" @click="logout">Tancar sessió</button>
            </div>
          </div>
        </div>

        <div class="section-favorites">
          <h2 class="title">Els teus preferits ⭐</h2>

          <div v-if="favorits.length" class="recomanats-grid">
            <div class="card" v-for="el in favorits" :key="el.id" @click="anarAPatrimoni(el.id)">
              <div class="img">
                <img v-if="getImatge(el)" :src="getImatge(el)" alt="Imatge patrimoni" />
                <div v-else class="placeholder">Sense imatge</div>
              </div>
              <h3>{{ el.titol }}</h3>
              <p>{{ el.municipi_nom }}</p>
            </div>
          </div>

          <div v-else class="no-favorites">
            <p>Encara no tens cap patrimoni preferit. Explora i marca'ls amb una estrella!</p>
            <button @click="$router.push('/cataleg')" class="btn-explore">Explorar catàleg</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { auth } from "@/firebase";
import { currentUser } from "@/store/userStore";
import { sendPasswordResetEmail } from "firebase/auth";
import axios from "axios";

export default {
  name: "ZonaPersonal",
  data() {
    return {
      cargando: true,
      usuari: { nom: "", email: "" },
      favorits: [],
      numComentaris: 0
    };
  },
  methods: {
    async logout() {
      try {
        await auth.signOut();
        currentUser.value = null;
        this.$router.push("/");
      } catch (err) { console.error(err); }
    },
    anarAPatrimoni(codi) {
      this.$router.push({ name: "patrimoni", query: { id: codi } });
    },
    getImatge(el) {
      if (!el.images || !el.images.length) return null;
      return el.images[0].split("|")[0];
    },
    async restablirContrasenya() {
      try {
        await sendPasswordResetEmail(auth, this.usuari.email);
        alert("S'ha enviat un correu de recuperació.");
      } catch (e) { alert("Error enviant el correu."); }
    }
  },
  async mounted() {
    auth.onAuthStateChanged(async (user) => {
      if (!user) {
        this.$router.push("/login");
        return;
      }
      try {
        const token = await user.getIdToken();
        const config = { headers: { Authorization: `Bearer ${token}` } };

        const resProfile = await axios.get("http://localhost:8080/api/auth/profile", config);
        this.usuari.nom = resProfile.data.nom_usuari;
        this.usuari.email = resProfile.data.email;
        this.numComentaris = resProfile.data.numComentaris;

        const resFavs = await axios.get("http://localhost:8080/api/favorite/getfavorites", config);
        this.favorits = resFavs.data || [];
      } catch (error) {
        console.error(error);
      } finally {
        this.cargando = false;
      }
    });
  }
};
</script>

<style scoped>
/* COPIAT DE L'ESTIL HOME */
.perfil-header {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px 0;
  background-color: #fcfcfc;
  border-bottom: 1px solid #eee;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.logo {
  width: 80px;
  height: auto;
  filter: drop-shadow(0px 4px 4px rgba(0, 0, 0, 0.1));
}

h1 {
  font-size: 2.2rem;
  color: #9e2828;
  margin: 0;
  font-weight: 700;
}

.perfil-content {
  padding: 30px;
  max-width: 1100px;
  margin: 0 auto;
}

.user-main-card {
  background: white;
  border-radius: 16px;
  padding: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.05);
  border: 1px solid #eee;
  margin-bottom: 50px;
}

.user-header-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar-circle {
  width: 80px;
  height: 80px;
  background: #f0f0f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
}

.user-text h2 {
  margin: 0;
  color: #333;
}

.email-text {
  color: #666;
  margin: 5px 0 0 0;
}

.user-stats-row {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
}

.mini-stat {
  text-align: center;
}

.mini-stat strong {
  display: block;
  font-size: 1.5rem;
  color: #9e2828;
}

.mini-stat span {
  font-size: 0.8rem;
  color: #999;
  text-transform: uppercase;
}

.buttons-group {
  display: flex;
  gap: 10px;
}

.btn-secondary {
  background: #f0f0f0;
  border: none;
  padding: 10px 15px;
  border-radius: 8px;
  cursor: pointer;
  color: #555;
  font-weight: 600;
}

.btn-logout {
  background: #333;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
}

.title {
  margin-bottom: 25px;
  font-size: 1.8rem;
  color: #333;
}

.recomanats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
}

.card {
  background: white;
  border-radius: 12px;
  padding: 12px;
  cursor: pointer;
  border: 1px solid #eee;
  transition: transform 0.3s, box-shadow 0.3s;
}

.card:hover {
  transform: translateY(-6px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
}

.img {
  height: 140px;
  border-radius: 8px;
  overflow: hidden;
  background: #eee;
  margin-bottom: 10px;
}

.img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card h3 {
  font-size: 1.1rem;
  margin: 10px 0 5px 0;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card p {
  font-size: 0.9rem;
  color: #777;
  margin: 0;
}

.no-favorites {
  padding: 40px;
  background: #f9f9f9;
  border-radius: 12px;
  text-align: center;
  color: #666;
  border: 1px dashed #ccc;
}

.btn-explore {
  margin-top: 15px;
  background: #9e2828;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
}
</style>