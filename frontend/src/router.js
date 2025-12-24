import { createRouter, createWebHistory } from "vue-router";
import Home from "@/views/Home.vue";
import Resultats from "@/views/Resultats.vue";
import InfoPatrimoni from "@/views/InfoPatrimoni.vue";
import Comarques from "@/views/Comarques.vue";
import Segles from "@/views/Segles.vue"
import Filtre from "@/views/Filtre.vue";

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
  {
    path: "/comarques",
    name: "comarques",
    component: Comarques,
    props: true,
  },
  {
    path: "/segles",
    name: "segles",
    component: Segles,
    props: true,
  },
  {
    path: "/filtre",
    name: "filtre",
    component: Filtre,
    props: true,
  },
];

export default createRouter({
  history: createWebHistory(),
  routes,
});
