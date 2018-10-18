<template>
  <section class="hero is-bold app-navbar animated" :class="{ slideInDown: show, slideOutDown: !show }">
    <div class="hero-head">
      <nav class="nav">
        <div class="nav-left">
          <a class="nav-item is-hidden-tablet" @click="toggleSidebar(!sidebar.opened)">
            <i class="fa fa-bars" aria-hidden="true"></i>
          </a>
        </div>
        <div class="nav-center">
          <a class="nav-item hero-brand" href="/">
            <img src="~assets/logo.png" :alt="pkginfo.description">
            <!--<tooltip :label="'v' + pkginfo.version" placement="right" type="success" size="small" :no-animate="true" :always="true" :rounded="true">-->
              <div class="is-hidden-mobile">
                <span class="vue">Icebreaker</span><strong class="admin">Admin</strong>
              </div>
            <!--</tooltip>-->
          </a>
        </div>
        <div class="nav-right ">
          <a class="nav-item nav-menu">
          <template v-if="username ===''">
            <a class="button is-primary modal-button" @click="login()">Log In</a>
          </template>
          <template v-else>
          <a class="nav-item ">
            <figure class="image is-16x16" style="margin-right: 8px;">
               <img src="http://bulma.io/images/jgthms.png">
           </figure>
          {{username}}
          </a>
          <a class="nav-item is-tab" @click="logout()"><span class="icon is-small"><i class="fa fa-power-off" style="margin-right: 6px;"></i></span><span>Log out</span></a>
          </template>  
          </a> 
          <log-out :visible='logoutvisible' @ok="logoutvisible = false" @cancel="logoutvisible = false" @close="logoutvisible = false"></log-out> 
          <log-in :visible='loggedin' @ok="loggedin = false" @cancel="loggedin = false" @close="loggedin = false"></log-in> 
          
        </div>
      </nav>
    </div> 
  </section>
</template>

<script>
import Tooltip from 'vue-bulma-tooltip'
import { mapGetters, mapActions } from 'vuex'
import LogIn from './LogIn'
import LogOut from './LogOut'

export default {

  components: {
    Tooltip,
    LogIn,
    LogOut
  },

  props: {
    show: Boolean,
    loggedin: Boolean,
    logoutvisible: Boolean
  },
  data () {
    return {
      loggedin: false,
      logoutvisible: false
    }
  },
  computed: mapGetters({
    pkginfo: 'pkg',
    sidebar: 'sidebar',
    username: 'loguser'
  }),
  methods: {
    ...mapActions(['toggleSidebar']),

    login (index, row) {
      this.loggedin = true
      console.log(row)
      console.log(index)
    },
    logout () {
      this.logoutvisible = true
    }
  }
}
</script>

<style lang="scss">
@import '~bulma/sass/utilities/variables';

.app-navbar {
  position: fixed;
  min-width: 100%;
  z-index: 1024;
  box-shadow: 0 2px 3px rgba(17, 17, 17, 0.1), 0 0 0 1px rgba(17, 17, 17, 0.1);

  .container {
    margin: auto 10px;
  }

  .nav-right {
    align-items: stretch;
    align-items: stretch;
    flex: 1;
    justify-content: flex-end;
    overflow: hidden;
    overflow-x: auto;
    white-space: nowrap;
  }
}

.hero-brand {
  .vue {
    margin-left: 10px;
    color: #36AC70;
  }
  .admin {
    color: #28374B;
  }
}
</style>
