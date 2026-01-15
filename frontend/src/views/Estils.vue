<template>
  <div class="estils-wrapper">
    <h1>Estils de Patrimoni</h1>
    <p class="subtitle">
      Descobreix el patrimoni segons el seu estil històric
    </p>

    <div class="grid">
      <div class="card" v-for="estil in estils" :key="estil.nom" @click="go(estil.nom)">
        <img :src="estil.img" :alt="estil.nom" />
        <div class="overlay">
          <h3>{{ estil.nom }}</h3>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Estils",

  data() {
    return {
      estils: [
        { nom: "Antic", img: "/img/estils/Estil-antic.jpg" },
        { nom: "Barroc", img: "/img/estils/Estil-barroc.jpg" },
        { nom: "Contemporani", img: "/img/estils/Estil-contemporani.jpg" },
        { nom: "Eclecticisme", img: "/img/estils/Estil-eclecticisme.jpg" },
        { nom: "Edats dels Metalls", img: "/img/estils/Estil-edats-metalls.jpg" },
        { nom: "Gòtic", img: "/img/estils/Estil-gotic.jpg" },
        { nom: "Historicista", img: "/img/estils/Estil-historicista.jpg" },
        { nom: "Ibèric", img: "/img/estils/Estil-iberic.jpg" },
        { nom: "Medieval", img: "/img/estils/Estil-medieval.jpg" },
        { nom: "Modern", img: "/img/estils/Estil-modern.jpg" },
        { nom: "Modernisme", img: "/img/estils/Estil-modernisme.jpg" },
        { nom: "Neoclàssic", img: "/img/estils/Estil-neoclassic.jpg" },
        { nom: "Neolític", img: "/img/estils/Estil-neolitic.jpg" },
        { nom: "Noucentisme", img: "/img/estils/Estil-noucentisme.jpg" },
        { nom: "Paleocristià", img: "/img/estils/Estil-paleocristia.jpg" },
        { nom: "Popular", img: "/img/estils/Estil-popular.jpg" },
        { nom: "Pre-romànic", img: "/img/estils/Estil-pre-romanic.jpg" },
        { nom: "Prehistòric", img: "/img/estils/Estil-prehistoric.jpg" },
        { nom: "Racionalisme", img: "/img/estils/Estil-racionalisme.jpg" },
        { nom: "Renaixement", img: "/img/estils/Estil-renaixement.jpg" },
        { nom: "Romà", img: "/img/estils/Estil-roma.jpg" },
        { nom: "Romànic", img: "/img/estils/Estil-romanic.jpg" },
        { nom: "Visigot", img: "/img/estils/Estil-visigot.jpg" },
      ],
    };
  },

  methods: {
    go(estil) {
      this.$router.push({
        name: "filtre",
        query: { 
          filtre: 'estil',
          nom_filtre: estil,
          data: estil }
      });
    }
  },
};
</script>

<style scoped>
.estils-wrapper {
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
  background: linear-gradient(to top, rgba(0, 0, 0, 0.6), transparent);
  display: flex;
  align-items: flex-end;
  padding: 14px;
}

.overlay h3 {
  color: white;
  font-size: 16px;
  font-weight: bold;
}
</style>
