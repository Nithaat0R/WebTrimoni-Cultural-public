<template>
  <div class="timeline-container">
    <div class="timeline">
      <div v-for="(any, index) in anys" :key="any" class="tick-wrapper">
        <!-- Segmento clicable -->
        <div v-if="index < anys.length - 1" class="segment"
          :class="{ activa: seleccionat === `${any}-${anys[index + 1]}` }"
          @click="seleccionar(any)"></div>

        <!-- Marca -->
        <div class="tick">
          <div class="line"></div>
          <span class="label">{{ any }}</span>
        </div>
      </div>
    </div>
  </div>
</template>



<script>
export default {
  name: "Segles",
  data() {
    return {
      anys: [
        1300, 1400, 1500, 1600, 1700,
        1800, 1900, 2000, 2026
      ],
      segles: {
        1300: 'XIV',
        1400: 'XV',
        1500: 'XVI',
        1600: 'XVII',
        1700: 'XVIII',
        1800: 'XIX',
        1900: 'XX',
        2000: 'XXI'
      },
      seleccionat: null
    };
  },
  methods: {
    seleccionar(inici) {

      this.$router.push({
        name: "filtre",
        query: { 
          filtre: 'segle',
          nom_filtre: this.segles[inici],
          data: this.segles[inici]}
      });
    }
  }
};
</script>

<style>
.timeline-container {
  padding: 40px 20px;
  background: #fff;
}

.timeline {
  display: flex;
  align-items: flex-start;
}

/* Cada bloque ocupa lo mismo */
.tick-wrapper {
  position: relative;
  flex: 1;
}

/* Marca */
.tick {
  text-align: center;
  position: relative;
  z-index: 2;
}

.line {
  width: 2px;
  height: 20px;
  background: #333;
  margin: 0 auto;
}

.label {
  margin-top: 8px;
  font-size: 14px;
}

/* Segmento ENTRE marcas */
.segment {
  position: absolute;
  top: 6px;
  left: 50%;
  width: 100%;
  height: 12px;              /* MÁS ALTO */
  background: #e0e0e0;
  cursor: pointer;
  z-index: 10;               /* 🔥 MUY IMPORTANTE */
  pointer-events: auto;      /* 🔥 */
}

.segment:hover {
  background: #81c784;
}


/* Activo */
.segment.activa {
  background: #388e3c;
}
</style>
