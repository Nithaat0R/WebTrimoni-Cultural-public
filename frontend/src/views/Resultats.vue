<template>
  <div class="resultats">
    <h1>Resultats per "{{ query }}"</h1>

    <div v-if="loading" class="loading">Cercant...</div>

    <div v-else-if="resultats.length === 0" class="empty">
      No s'han trobat resultats
    </div>

    <div class="cards" v-else>
      <div
        class="card"
        v-for="el in resultats"
        :key="el.id"
        @click="go(el.id)"
      >
        <div class="image">
          <img
            v-if="getImatge(el)"
            :src="getImatge(el)"
            alt="Imatge patrimoni"
          />
          <div v-else class="placeholder">Sense imatge</div>
        </div>

        <div class="info">
          <h3>{{ el.titol }}</h3>
          <p>📍{{ el.municipi_nom }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Resultats",
  data() {
    return {
      query: "",
      resultats: [],
      loading: false,
    };
  },
  mounted() {
    this.query = this.$route.query.q;
    if (this.query) {
      this.cerca();
    }
  },
  methods: {
    async cerca() {
      this.loading = true;
      try {
        const response = await axios.get("http://localhost:8080/api/search", {
          params: { search: this.query },
        });
        this.resultats = response.data.elements ?? response.data;
      } catch (e) {
        console.error(e);
      } finally {
        this.loading = false;
      }
    },

    go(id) {
      this.$router.push({
        name: "patrimoni",
        query: { id },
      });
    },

    getImatge(el) {
      if (!el.images || el.images.length === 0) return null;
      return el.images[0].split("|")[0];
    },
  },
};
</script>

<style>
.resultats {
  padding: 20px;
}

.resultats h1 {
  margin-bottom: 20px;
  color: #000000;
}

/* GRID */
.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

/* CARD */
.card {
  background: rgb(255, 243, 243);
  border-radius: 14px;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid rgba(255, 111, 111, 0.35);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 25px rgba(255, 111, 111, 0.35);
}

/* IMAGEN */
.image {
  height: 160px;
  background: #ffd6d6;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.placeholder {
  color: #ff6f6f;
  font-weight: 600;
}

/* INFO */
.info {
  padding: 14px;
  color: #2c2c2c;
}

.info h3 {
  margin: 0 0 8px;
  color: #000000;
  font-size: 1.1rem;
}

.info p {
  margin: 4px 0;
  color: #555;
  font-size: 0.9rem;
}

/* ESTADOS */
.loading,
.empty {
  margin-top: 20px;
  color: #000000;
  font-style: italic;
}
</style>
