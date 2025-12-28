<template>
  <div>
    <h1>Benvingut a WebTrimoni</h1>
    <p>Cerca patrimoni cultural de Catalunya.</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "App",
  data() {
    return {
      current: "inici",
      search: "",
      resultats: [],
    };
  },
  methods: {
  go(section) {
    this.current = section;
  },
  cerca() {
    if (!this.search) return;
    this.$router.push({ name: "search", query: { q: this.search } });
  }
  },
};
</script>

<style>
.header {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 10px 20px;
  background: #ffffff;
  border-bottom: 1px solid #ddd;
}

.logo-btn {
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  background: none;
  border: none;
}

nav button {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  padding: 8px 12px;
}

nav button:hover {
  background: #e0e0e0;
  border-radius: 5px;
}

.main {
  padding: 20px;
}
.search-bar {
  padding: 20px;
  background: #fff;
  border-bottom: 1px solid #ddd;
}

.search-bar input {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

</style>
