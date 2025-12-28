<template>
  <div class="info-wrapper" v-if="element">
    <div class="card">

      <div class="image">
        <img v-if="primeraImatge" :src="primeraImatge" alt="Imatge del patrimoni" />
        <div v-else class="placeholder">Sense imatge</div>
      </div>
      <div class="data">
        <h2>{{ element.titol }}</h2>

        <p><strong>Codi:</strong> {{ element.codi_element }}</p>
        <p><strong>Ubicació:</strong> {{ element.ubicacio }}</p>
        <p><strong>Segle:</strong> {{ element.centuria }}</p>

        <div class="desc" v-html="element.descripcio"></div>
      </div>

    </div>
  </div>

  <p v-else class="loading">Carregant informació…</p>
</template>

<script>
import axios from "axios";

export default {
  name: "InfoPatrimoni",

  data() {
    return {
      element: null,
    };
  },

  async mounted() {
    const id = this.$route.query.id;

    if (!id) {
      console.error("No hi ha id a la URL");
      return;
    }

    try {
      const res = await axios.get(
        "http://localhost:8080/api/infopatrimoni",
        {
          params: { id }
        }
      );
      this.element = res.data;
    } catch (e) {
      console.error("Error carregant patrimoni", e);
    }
  },
  computed: {
    primeraImatge() {
      if (!this.element) return null;
      if (!this.element.images) return null;
      if (!this.element.images.length) return null;

      return this.element.images[0].split("|")[0];
    }
  }
};
</script>

<style scoped>
.info-wrapper {
  padding: 20px;
}

.card {
  display: flex;
  gap: 20px;
  background: #ff8f8f;
  border-radius: 12px;
  padding: 20px;
}

.image {
  width: 240px;
  height: 180px;
  background: #fff;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder {
  color: #999;
}

.data {
  flex: 1;
}

.desc {
  margin-top: 10px;
  background: #ffd6d6;
  padding: 10px;
  border-radius: 8px;
}

.loading {
  padding: 30px;
  font-size: 18px;
}
</style>
