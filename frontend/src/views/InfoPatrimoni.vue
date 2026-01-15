<template>
  <div class="info-wrapper" v-if="element">
    <div class="info-card">

      <div class="image-wrapper">
        <div class="info-image">
          <img v-if="primeraImatge" :src="primeraImatge" class="info-img" alt="Imatge del patrimoni" />
          <div v-else class="placeholder">Sense imatge</div>
        </div>

        <button v-if="estaLogejat" :class="['fav-btn', { 'is-fav': isFavorite }]" @click="canviarPreferit">
          {{ isFavorite ? '⭐ Treure de preferits' : '☆ Afegir a preferits' }}
        </button>
        <button v-else class="fav-btn" @click="canviarPreferit">
          ❌ Per afegir a preferits inicia sessió
        </button>
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

  <div class="comments">

    <div class="comment-form">
      <h3>Afegir comentari</h3>

      <div class="stars">
        <span v-for="n in 5" :key="n" class="star" :class="{ active: newRating >= n }" @click="newRating = n">
          ★
        </span>
      </div>

      <textarea v-model="newComment" placeholder="Escriu el teu comentari..."></textarea>

      <button @click="submitComment">
        Publicar comentari
      </button>
    </div>

    <div class="comment-list">
      <h3>Comentaris</h3>

      <div v-if="comments.length === 0" class="no-comments">
        Encara no hi ha comentaris
      </div>

      <div v-for="c in comments" :key="c.id" class="comment">
        <div class="comment-header">
          <div class="stars small">
            <span v-for="n in 5" :key="n" :class="{ active: c.puntuacio >= n }">
              ★
            </span>
          </div>
          <span class="date">{{ formatDate(c.data) }}</span>
        </div>

        <strong style="font-size: 13px; color: #e04545;">{{ c.nomUsuari }}</strong>

        <p class="comment-text">{{ c.comentari }}</p>
      </div>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import { currentUser } from "@/store/userStore";

export default {
  name: "InfoPatrimoni",
  data() {
    return {
      element: null,
      isFavorite: false,
      comments: [],
      newRating: 0,
      newComment: "",
    };
  },
  methods: {
    async canviarPreferit() {
      if (!this.estaLogejat) {
        alert("Inicia sessió per afegir a preferits");
        return;
      }
      let url;

      if (this.isFavorite) {
        url = "http://localhost:8080/api/favorite/remove";
      } else {
        url = "http://localhost:8080/api/favorite/add";
      }

      try {
        const respuesta = await axios.post(
          url,
          { idPatrimoni: this.element.id },
          {
            headers: {
              Authorization: `Bearer ${currentUser.value.accessToken}`
            }
          }
        );
        console.log("Respuesta del servidor:", respuesta.data);
        this.isFavorite = !this.isFavorite;

      } catch (error) {
        console.error("Error al actualizar favoritos:", error);
      }
    }, async checkFavoriteStatus() {
      if (!currentUser.value || !this.element) return;

      try {
        const res = await axios.get("http://localhost:8080/api/favorite/state", {
          params: { idPatrimoni: this.element.id },
          headers: { Authorization: `Bearer ${currentUser.value.accessToken}` }
        });
        console.log(res.data.isFavorite)
        this.isFavorite = res.data.isFavorite;
      } catch (e) {
        console.error("Error obtenint l'estat de preferit", e);
      }
    },
    async loadComments() {
      const id = this.$route.query.id;

      if (!id) return;

      try {
        const res = await axios.get(
          "http://localhost:8080/api/comments/load",
          { params: { id: id } }
        );
        this.comments = res.data;
      } catch (e) {
        console.error("Error carregant comentaris", e);
      }
    },
    async submitComment() {
      if (this.newRating === 0) {
        alert("Selecciona una puntuació");
        return;
      } else if (!this.estaLogejat) {
        alert("Inicia sessió per poder afegir comentaris");
        return;
      }

      const comment = {
        idPatrimoni: this.element.id,
        rating: this.newRating,
        comment: this.newComment,
        date: new Date().toISOString()
      };

      try {
        console.log("S'envia el comentari")
        const respuesta = await axios.post("http://localhost:8080/api/comments/submit",
          comment,
          {
            headers: {
              Authorization: `Bearer ${currentUser.value.accessToken}`
            }
          }
        );

        this.newRating = 0;
        this.newComment = "";
        
        this.loadComments();
      } catch (e) {
        console.error("Error pujant el comentari", e);
      }

    },

    formatDate(date) {
      const d = new Date(date);

      return d.toLocaleDateString("ca-ES", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit"
      });
    }

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

      await this.checkFavoriteStatus();
      this.loadComments();

    } catch (e) {
      console.error("Error carregant patrimoni", e);
    }
  },
  computed: {
    estaLogejat() {
      return currentUser.value != null
    },
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

  flex-shrink: 0;
}

.info-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* 🔑 */
  display: block;
}

.image-wrapper {
  width: 300px;
  display: flex;
  flex-direction: column;
  gap: 8px;
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
}

.info-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.fav-btn {
  width: 100%;
  padding: 8px;
  background: #e04545;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
}

.fav-btn:hover {
  background: #c83b3b;
}

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

.comments {
  margin-top: 30px;
  background: #ffd6d6;
  padding: 20px;
  border-radius: 12px;
}

.comment-form {
  margin-bottom: 30px;
}

.comment-form textarea {
  width: 100%;
  min-height: 80px;
  margin-top: 10px;
  padding: 10px;
  border-radius: 8px;
  border: none;
  resize: vertical;
}

.comment-form button {
  margin-top: 10px;
  padding: 10px 16px;
  background: #e04545;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.comment-form button:hover {
  background: #c83b3b;
}

/* ESTRELLAS */
.stars {
  font-size: 24px;
  cursor: pointer;
}

.stars.small {
  font-size: 16px;
}

.star,
.stars span {
  color: #ccc;
}

.star.active,
.stars span.active {
  color: #ffcc00;
}

/* COMENTARIOS */
.comment {
  background: #fff;
  padding: 12px;
  border-radius: 10px;
  margin-bottom: 10px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-text {
  margin-top: 6px;
  font-size: 14px;
}

.date {
  font-size: 12px;
  color: #777;
}

.no-comments {
  font-style: italic;
  color: #666;
}
</style>
