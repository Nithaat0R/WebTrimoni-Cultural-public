<template>
  <div class="home">
    <header class="home-header">
      <div class="header-content">
        <img src="@/assets/LOGO PATRIMONI.png" alt="WebTrimoni Logo" class="logo" />
        <h1>WebTrimoni Cultural</h1>
      </div>
    </header>

    <h2 class="title">Patrimonis destacats</h2>

    <div class="carousel" v-if="patrimonis.length">
      <button class="nav left" @click="prev">‹</button>
      <div class="viewport">
        <div class="track" :style="{ transform: `translateX(-${offset}px)` }">
          <PatrimoniCard 
            v-for="el in patrimonis" 
            :key="el.id" 
            :patrimoni="el" 
            class="carousel-card-fix"
            @click="go"
          />
        </div>
      </div>
      <button class="nav right" @click="next">›</button>
    </div>

    <p v-else class="loading">Carregant patrimonis…</p>

    <div style="margin-top: 50px;">
      <h2 class="title">Recomanat per a tu</h2>

      <div v-if="recomanats.length" class="recomanats-grid">
        <PatrimoniCard 
          v-for="el in recomanats" 
          :key="el.id" 
          :patrimoni="el" 
          @click="go"
        />
      </div>

      <div v-else-if="isLoggedIn" class="no-recomanats">
        <p>Encara no tenim recomanacions per a tu. Puntua alguns patrimonis perquè puguem conèixer els teus gustos!</p>
      </div>

      <div v-else class="no-recomanats">
        <p>Inicia sessió per veure recomanacions personalitzades.</p>
      </div>
    </div>

    <div v-if="isLoggedIn" style="margin-top: 50px;">
      <h2 class="title">Els teus preferits ⭐</h2>

      <div v-if="favorits.length" class="recomanats-grid">
        <PatrimoniCard 
          v-for="el in favorits" 
          :key="el.id" 
          :patrimoni="el" 
          @click="go"
        />
      </div>

      <div v-else class="no-recomanats">
        <p>Encara no tens cap patrimoni preferit. Explora i marca'ls amb una estrella!</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { currentUser } from "@/store/userStore";
import { watch } from "vue";
import PatrimoniCard from "@/components/PatrimoniCard.vue";

export default {
  name: "Home",
  components: {
    PatrimoniCard
  },
  data() {
    return {
      patrimonis: [],
      recomanats: [],
      favorits: [],
      currentIndex: 0,
      visibleCards: 4,
      cardWidth: 240,
      gap: 16,
      interval: null,
    };
  },
  computed: {
    isLoggedIn() {
      return !!currentUser.value;
    },
    maxIndex() {
      return Math.max(0, this.patrimonis.length - this.visibleCards);
    },
    dotsCount() {
      return this.maxIndex + 1;
    },
    offset() {
      return this.currentIndex * (this.cardWidth + this.gap);
    },
  },
  mounted() {
    this.load();
    watch(
      () => currentUser.value,
      (user) => {
        this.loadRecommendations(user);
        this.loadFavorites(user);
      },
      { immediate: true }
    );
  },
  beforeUnmount() {
    clearInterval(this.interval);
  },
  methods: {
    async load() {
      try {
        const res = await axios.get("http://localhost:8080/api/featured");
        this.patrimonis = res.data;
        if (this.patrimonis.length > 0) {
          this.startAutoplay();
        }
      } catch (e) {
        console.error("Error carregant patrimonis destacats", e);
      }
    },
    async loadRecommendations(user) {
      try {
        const config = {};
        if (user?.accessToken) {
          config.headers = { Authorization: `Bearer ${user.accessToken}` };
        }
        const res = await axios.get("http://localhost:8080/api/recommendations", config);
        this.recomanats = res.data;
      } catch (e) {
        console.error("Error recomanacions:", e);
      }
    },
    async loadFavorites(user) {
      if (!user) {
        this.favorits = [];
        return;
      }
      try {
        const config = {
          headers: { Authorization: `Bearer ${user.accessToken}` }
        };
        const res = await axios.get("http://localhost:8080/api/favorite/getfavorites", config);
        this.favorits = res.data;
      } catch (e) {
        console.error("Error carregant preferits:", e);
      }
    },
    startAutoplay() {
      if (this.patrimonis.length <= this.visibleCards) return;
      this.interval = setInterval(this.next, 4000);
    },
    next() {
      if (this.maxIndex === 0) return;
      this.currentIndex = this.currentIndex >= this.maxIndex ? 0 : this.currentIndex + 1;
    },
    prev() {
      if (this.maxIndex === 0) return;
      this.currentIndex = this.currentIndex <= 0 ? this.maxIndex : this.currentIndex - 1;
    },
    go(id) {
      this.$router.push({ name: "patrimoni", query: { id } });
    }
  },
};
</script>

<style scoped>
.home-header {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px 0;
  background-color: #fcfcfc;
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 20px; 
}

.logo {
  width: 100px;
  height: auto;
  filter: drop-shadow(0px 4px 4px rgba(0, 0, 0, 0.1));
}

h1 {
  font-size: 2.5rem;
  color: #9e2828;
  margin: 0;
  font-weight: 700;
  letter-spacing: -1px;
}

.home {
  padding: 30px;
}

.title {
  margin-bottom: 20px;
}

.carousel {
  display: flex;
  align-items: center;
  position: relative;
}

.viewport {
  overflow: hidden;
  width: 100%;
}

.track {
  display: flex;
  gap: 16px;
  transition: transform 0.5s ease;
}

.carousel-card-fix {
  width: 240px !important;
  flex-shrink: 0;
}

.nav {
  background: none;
  border: none;
  font-size: 32px;
  cursor: pointer;
  padding: 10px;
  color: #b22222;
}

.loading {
  margin-top: 20px;
  font-size: 16px;
  color: #555;
  text-align: center;
}

.recomanats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
}

.no-recomanats {
  padding: 20px;
  background: #f9f9f9;
  border-radius: 12px;
  text-align: center;
  color: #666;
  border: 1px dashed #ccc;
}
</style>