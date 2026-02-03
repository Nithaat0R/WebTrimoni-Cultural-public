<template>
  <div class="resultats">
    <h1>Resultats per "{{ query }}"</h1>

    <div v-if="loading" class="loading">Cercant...</div>

    <div v-else-if="resultats.length === 0" class="empty">
      No s'han trobat resultats
    </div>

    <div class="cards" v-else>
      <PatrimoniCard
        v-for="el in resultats"
        :key="el.id"
        :patrimoni="el"
        @select="go"
      />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import PatrimoniCard from "@/components/PatrimoniCard.vue";

export default {
  name: "Resultats",
  components: {
    PatrimoniCard
  },
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
    }
  },
};
</script>

<style scoped>
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

/* ESTADOS */
.loading,
.empty {
  margin-top: 20px;
  color: #000000;
  font-style: italic;
}
</style>