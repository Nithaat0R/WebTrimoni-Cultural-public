<template>
  <div class="info-wrapper" v-if="element">
    <div class="info-card">

      <div class="info-image">
        <img v-if="primeraImatge" :src="primeraImatge" class="info-img" alt="Imatge del patrimoni" />
        <div v-else class="placeholder">Sense imatge</div>
      </div>
      <div class="data">
        <h2>{{ element.titol }}</h2>

        <div class="info-group">
          <p><strong>Any:</strong> {{ element.any }}</p>
          <p><strong>Segle:</strong> {{ element.centuria }}</p>
          <p><strong>Àmbit de patrimoni:</strong> {{ element.ambit }}</p>
          <p><strong>Tipologia:</strong> {{ element.tipologia }}</p>
        </div>

        <div class="info-group">
          <p><strong>Estil / Època:</strong> {{ element.estil }}</p>
          <p><strong>Estat de conservació:</strong> {{ element.estat_conservacio }}</p>
          <p><strong>Accés:</strong> {{ element.acces }}</p>
          <p><strong>Titularitat:</strong> {{ element.titularitat }}</p>
        </div>

        <div class="info-group">
          <p><strong>Ubicació:</strong> {{ element.ubicacio }}</p>
          <p><strong>Coordenades:</strong> {{ element.latitud }}, {{ element.longitud }}</p>
        </div>

        <div class="info-group">
          <p><strong>Número de fitxa:</strong> {{ element.codi_element }}</p>
        </div>

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

.info-card {
  display: flex;
  gap: 20px;
  background: #ff8f8f;
  border-radius: 12px;
  padding: 20px;
}

.info-image {
  width: 300px;
  height: 250px;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;

  flex-shrink: 0; /* 🔑 ESTA ES LA CLAVE */
}


/* LA IMAGEN */
.info-img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 🔑 */
  display: block;
}

/* Placeholder si no hay imagen */
.placeholder {
  color: #999;
  font-size: 14px;
}


.data {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.info-group {
  padding: 2px;
  border-radius: 10px;
}

.info-group p {
  margin: 3px;
  font-size: 14px;
}

.info-group strong {
  color: #000000;
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
