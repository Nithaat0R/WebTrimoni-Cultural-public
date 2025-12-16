import { createRouter, createWebHistory } from "vue-router";
import Home from "@/views/Home.vue";
import Resultats from "@/views/Resultats.vue";
import InfoPatrimoni from "@/views/InfoPatrimoni.vue";

const routes = [
  {
    path: "/",
    name: "home",
    component: Home,
  },
  {
    path: "/search",
    name: "search",
    component: Resultats,
  },
  {
    path: "/patrimoni",
    name: "patrimoni",
    component: InfoPatrimoni,
    props: true,
  },
];

export default createRouter({
  history: createWebHistory(),
  routes,
});
