<template>
  <div>
    <card-modal :visible="visible" @ok="ok" @cancel="cancel" @close="close" :title="title" transition="zoom">
      <app-list></app-list>
    </card-modal>
  </div>
</template>

<script>

  import { CardModal } from 'vue-bulma-modal'
  import AppList from './AppList'
  import _ from 'lodash'

  export default {
    props: {
      visible: Boolean,
      title: String,
      url: String,
      appliance: {
        type: Object,
        default: () => {
          return {
            updatedAt: '2016-05-03',
            createdAt: '2016-05-03',
            hostName: 'nbapp5as',
            lab: 'mtv',
            ip: '10.220.182.6',
            specification: {
              model: '5230',
              type: 'physical', // or vm
              sn: 'NNG05122510452'.trim().toLocaleLowerCase()
            },
            usageStats: {
              lastUpdated: '2016-05-03'
              // to add more fields
            },
            supportedScenarios: ['security', 'isci', '.....'],
            reservation: {
              from: '2016-05-03',
              to: '2017-05-03',
              piInfo: 'tbd',
              owner: 'dean.zou',
              pipeline: 'nba_pipeline', // if for dev, set null
              team: 'icebreaker', // team or pipeline
              justification: 'for nba pipeline'
            },
            status: 0, // implemented as enum, assigned or idle, adding, testing
            notes: 'blabla'
          }
        }
      }
    },
    data () {
      return {
        links: [],
        state4: '',
        timeout: null,
        app: this.appliance,
        models: ['5230', '5330', '5340']
      }
    },
    components: {
      CardModal,
      AppList
    },

    computed: {
      modelSelects () {
        return _.map(this.models, (model) => { return {label: model, value: model} })
      }
    },

    methods: {
      querySearchAsync (queryString, cb) {
        cb(this.modelSelects)
      },
      handleSelect () {
      },
      open (url) {
        window.open(url)
      },
      close () {
        this.$emit('close')
      },
      ok () {
        console.log({'app': this.app})
        this.$emit('ok')
      },
      cancel () {
        this.$emit('cancel')
      }
    },

    created () {
      console.log({applinace: this.appliance})
    }
  }

</script>

<style lang="scss" scoped>
    @import '~element-ui/lib/theme-default/index.css'
</style>
