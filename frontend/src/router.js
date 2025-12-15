import { createRouter, createWebHistory } from "vue-router";
import Home from "./views/Home.vue";
import Resultats from "./views/Resultats.vue";

const routes = [
  { path: "/", name: "home", component: Home },
  { path: "/search", name: "search", component: Resultats, props: true }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
