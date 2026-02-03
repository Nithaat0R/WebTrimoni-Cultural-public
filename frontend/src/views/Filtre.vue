<template>
  <div class="resultats">
    <h1>Resultats filtrats per {{$route.query.filtre}}: {{$route.query.nom_filtre}} </h1>

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
body {
  margin: 0;
  font-family: Arial, sans-serif;
  background: #ffe0e0;
}

#app {
  min-height: 100vh;
}

/* ===== TOP BAR ===== */
.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #ff6f6f;
  padding: 10px 20px;
  gap: 20px;
}

.left {
  display: flex;
  align-items: center;
  gap:20px;
}

.logo-img {
  height: 60px;
  width: auto;
}

.logo {
  display: flex;
  align-items: center;
}

/* Menu */
.menu {
  display: flex;
  gap: 10px;
}

.menu button {
  background: #ff7f7f;
  border: none;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
}

.menu button:hover {
  background: #ff5c5c;
}

/* Actions */
.actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

/* Search */
.search {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 20px;
  padding: 6px 12px;
  gap: 6px;
}

.search input {
  border: none;
  outline: none;
  font-size: 14px;
}

/* User */
.user-icon {
  font-size: 20px;
  cursor: pointer;
}

/* Content */
.content {
  padding: 20px;
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