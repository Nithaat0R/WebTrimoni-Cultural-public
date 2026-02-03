<template>
  <div class="ambits-wrapper">
    <h1>Àmbits del Patrimoni</h1>
    <p class="subtitle">
      Explora el catàleg segons la tipologia del bé cultural
    </p>

    <div class="grid">
      <div class="card" v-for="ambit in ambits" :key="ambit.nom" @click="go(ambit.nom)">
        <img :src="ambit.img" :alt="ambit.nom" />
        <div class="overlay">
          <h3>{{ ambit.nom }}</h3>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Ambits",

  data() {
    return {
      // Ámbitos principales del Patrimonio Cultural
      ambits: [
        { nom: "Patrimoni immoble", img: "/img/ambits/immoble.jpg" },
        { nom: "Patrimoni moble", img: "/img/ambits/moble.jpg" },
        { nom: "Patrimoni documental", img: "/img/ambits/documental.jpg" },
        { nom: "Patrimoni immaterial", img: "/img/ambits/immaterial.jpg" },
        { nom: "Patrimoni natural", img: "/img/ambits/natural.jpg" }
      ],
    };
  },

  methods: {
    go(ambitNom) {
      this.$router.push({
        name: "filtre",
        query: { 
          filtre: 'ambit', 
          nom_filtre: ambitNom,
          data: ambitNom 
        }
      });
    }
  },
};
</script>

<style scoped>
.ambits-wrapper {
  padding: 30px;
}

.subtitle {
  margin-bottom: 25px;
  color: #555;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
}

.card {
  position: relative;
  height: 160px;
  border-radius: 14px;
  overflow: hidden;
  cursor: pointer;
  background: #eee;
  transition: transform 0.3s;
}

.card:hover {
  transform: translateY(-6px);
}

.card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
  display: flex;
  align-items: flex-end;
  padding: 14px;
}

.overlay h3 {
  color: white;
  font-size: 1.1rem;
  font-weight: bold;
  margin: 0;
}
</style>