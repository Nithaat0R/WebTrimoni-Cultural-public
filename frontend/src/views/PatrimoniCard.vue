<template>
  <div class="card">
    <div class="card-image">
      <img
        :src="imatge"
        alt="Imatge patrimoni"
      />
    </div>

    <div class="card-content">
      <h3>{{ item.titol }}</h3>

      <p><strong>Municipi:</strong> {{ item.municipi_nom }}</p>
      <p><strong>Àmbit:</strong> {{ item.ambit }}</p>
      <p><strong>Tipologia:</strong> {{ item.tipologia }}</p>
      <p><strong>Estil:</strong> {{ item.estil }}</p>
      <p><strong>Segle:</strong> {{ item.centuria }}</p>
      <p><strong>Coordenades:</strong> {{ item.coordenades }}</p>

      <a
        :href="item.url"
        target="_blank"
        class="link"
      >
        + informació
      </a>
    </div>
  </div>
</template>

<script>
export default {
  name: "PatrimoniCard",
  props: {
    item: {
      type: Object,
      required: true,
    },
  },
  computed: {
    imatge() {
      if (this.item.images && this.item.images.length > 0) {
        return this.item.images[0].split("|")[0];
      }
      return "https://via.placeholder.com/150";
    },
  },
};
</script>

<style scoped>
.card {
  display: flex;
  gap: 20px;
  background: #ff9a9a;
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 20px;
}

.card-image img {
  width: 150px;
  height: 150px;
  object-fit: cover;
  border-radius: 8px;
  background: #fff;
}

.card-content {
  flex: 1;
}

.card-content h3 {
  margin-top: 0;
  margin-bottom: 10px;
}

.card-content p {
  margin: 4px 0;
  font-size: 14px;
}

.link {
  display: inline-block;
  margin-top: 10px;
  color: #004aad;
  font-weight: bold;
  text-decoration: none;
}
</style>
