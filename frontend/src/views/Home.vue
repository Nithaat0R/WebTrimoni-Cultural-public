<template>
  <div class="home">
    <h1>WebTrimoni Cultural</h1>

    <h2 class="title">Patrimonis destacats</h2>

    <div class="carousel" v-if="patrimonis.length">
      <button class="nav left" @click="prev">‹</button>

      <div class="viewport">
        <div class="track" :style="{ transform: `translateX(-${offset}px)` }">
          <div
            class="card"
            v-for="el in patrimonis"
            :key="el.codi_element"
            @click="go(el.codi_element)"
          >
            <div class="img">
              <img
                v-if="getImatge(el)"
                :src="getImatge(el)"
                alt="Imatge patrimoni"
              />
              <div v-else class="placeholder">Sense imatge</div>
            </div>

            <h3>{{ el.titol }}</h3>
            <p>{{ el.municipi_nom }}</p>
          </div>
        </div>
      </div>

      <button class="nav right" @click="next">›</button>
    </div>

    <p v-else class="loading">Carregant patrimonis…</p>

    <div class="dots" v-if="dotsCount > 1">
      <span
        v-for="i in dotsCount"
        :key="i"
        :class="{ active: i - 1 === currentIndex }"
      />
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Home",
  data() {
    return {
      patrimonis: [],
      currentIndex: 0,
      visibleCards: 4,
      cardWidth: 240,
      gap: 16,
      interval: null,
    };
  },
  computed: {
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
  },
  beforeUnmount() {
    clearInterval(this.interval);
  },
  methods: {
    async load() {
      try {
        const res = await axios.get(
          "http://localhost:8080/api/search",
          { params: { search: "catalunya" } }
        );

        const data = Array.isArray(res.data)
          ? res.data
          : res.data.elements;

        this.patrimonis = (data ?? []).slice(0, 10);

        this.startAutoplay();
      } catch (e) {
        console.error("Error carregant patrimonis", e);
      }
    },
    startAutoplay() {
      if (this.patrimonis.length <= this.visibleCards) return;
      this.interval = setInterval(this.next, 4000);
    },
    next() {
      if (this.maxIndex === 0) return;
      this.currentIndex =
        this.currentIndex >= this.maxIndex ? 0 : this.currentIndex + 1;
    },
    prev() {
      if (this.maxIndex === 0) return;
      this.currentIndex =
        this.currentIndex <= 0 ? this.maxIndex : this.currentIndex - 1;
    },
    go(codi) {
      this.$router.push({ name: "patrimoni", query: { id: codi } });
    },
    getImatge(el) {
      if (!el.images || !el.images.length) return null;
      return el.images[0].split("|")[0];
    }
  },
};
</script>

<style scoped>
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

.card {
  width: 240px;
  background: white;
  border-radius: 12px;
  padding: 12px;
  cursor: pointer;
  flex-shrink: 0;
  transition: transform 0.3s;
}

.card:hover {
  transform: translateY(-6px);
}

.img {
  height: 140px;
  border-radius: 8px;
  overflow: hidden;
  background: #eee;
}

.img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.placeholder {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #777;
}

.nav {
  background: none;
  border: none;
  font-size: 32px;
  cursor: pointer;
  padding: 10px;
  color: #b22222;
}

.dots {
  margin-top: 12px;
  display: flex;
  justify-content: center;
  gap: 8px;
}

.dots span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #ccc;
}

.dots span.active {
  background: #b22222;
}

.loading {
  margin-top: 20px;
  font-size: 16px;
  color: #555;
  text-align: center;
}
</style>
