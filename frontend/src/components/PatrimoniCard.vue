<template>
  <div class="card" @click="handleClick">
    <div class="image-container">
      <img
        v-if="imageUrl"
        :src="imageUrl"
        alt="Imatge patrimoni"
        loading="lazy"
      />
      <div v-else class="placeholder">Sense imatge</div>
    </div>

    <div class="info">
      <h3>{{ patrimoni.titol }}</h3>
      <p>📍 {{ patrimoni.municipi_nom }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "PatrimoniCard",
  props: {
    patrimoni: {
      type: Object,
      required: true
    }
  },
  computed: {
    imageUrl() {
      const el = this.patrimoni;
      if (!el.images || el.images.length === 0) return null;
      // Extraemos la URL antes del pipe si existe
      return el.images[0].split("|")[0];
    }
  },
  methods: {
    handleClick() {
      // Emitimos un evento al padre para que él decida qué hacer (navegar)
      this.$emit("click", this.patrimoni.id);
    }
  }
};
</script>

<style scoped>
.card {
  background: rgb(255, 243, 243);
  border-radius: 14px;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid rgba(255, 111, 111, 0.35);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  width: 100%; /* Se adapta al contenedor */
  display: flex;
  flex-direction: column;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 25px rgba(255, 111, 111, 0.35);
}

.image-container {
  height: 160px;
  background: #ffd6d6;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.image-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.placeholder {
  color: #ff6f6f;
  font-weight: 600;
  font-size: 0.9rem;
}

.info {
  padding: 14px;
  color: #2c2c2c;
  flex-grow: 1;
}

.info h3 {
  margin: 0 0 8px;
  color: #000000;
  font-size: 1.1rem;
  line-height: 1.2;
}

.info p {
  margin: 4px 0;
  color: #555;
  font-size: 0.9rem;
}
</style>