<template>
  <div class="resultats">
    <h1>Resultats filtrats per {{$route.query.filtre}}: {{$route.query.nom_filtre}} </h1>

    <div v-if="loading">Cercant...</div>

    <div v-else>
      <div v-if="resultats.length === 0">
        No s'han trobat resultats
      </div>

      <div class="cards">
        <div @click="go(el.id)" class="card" v-for="el in resultats" :key="el.id">
          <h3>{{ el.titol }}</h3>
          <p>{{ el.municipi_nom }}</p>
          <p>{{ el.estil }}</p>
          <a :href="el.url" target="_blank">Veure fitxa</a>
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
    const { filtre, data } = this.$route.query;

    if (filtre && data) {
      this.cerca();
    }
  },
  methods: {
    async cerca() {
      this.loading = true;
      try {
        const response = await axios.get("http://localhost:8080/api/filtre", {
          params: {
            filtre: this.$route.query.filtre,
            data: this.$route.query.data
          }
        });
        console.log(response)
        this.resultats = response.data.elements ?? response.data;
      } catch (e) {
        console.error(e);
      } finally {
        this.loading = false;
      }
    },
    go(id) {
      console.log("CLICK EN:", id);
      this.$router.push({
        name: "patrimoni",
        query: { id }
      });
    }
  },
};
</script>

<style>
.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 16px;
}

.card {
  background: white;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #ddd;
}
</style>
