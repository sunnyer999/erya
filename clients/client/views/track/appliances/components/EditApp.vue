<template>
  <div>
    <card-modal :visible="visible" @ok="ok" @cancel="cancel" @close="close" :title="title" transition="zoom">
      <el-row>
        <el-col :span="12">
          <div class="field">
            <label class="label">Name</label>
            <p class="control">
              <el-autocomplete v-model="app.hostName" :fetch-suggestions="querySearchAsync" placeholder="Please input" @select="handleSelect"></el-autocomplete>
            </p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="field">
            <label class="label">Type</label>
            <el-radio class="radio" v-model="app.specification.type" label="physical">Physical</el-radio>
            <el-radio class="radio" v-model="app.specification.type" label="virtual machine">Virtual Machine</el-radio>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <div class="field">
            <label class="label">Lab</label>
            <p class="control">
              <el-autocomplete v-model="app.lab" :fetch-suggestions="querySearchLabAsync" placeholder="Please input" @select="handleSelect"></el-autocomplete>
            </p>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="field">
            <label class="label">Model</label>
            <el-autocomplete class="inline-input" v-model="app.specification.model" :fetch-suggestions="querySearchAsync" placeholder="Please Input"
              @select="handleSelect"></el-autocomplete>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="field">
            <label class="label">IP</label>
            <p class="control">
              <el-input v-model="app.ip" placeholder="Please input"></el-input>
            </p>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <div class="field">
            <label class="label">Notes</label>
            <el-autocomplete class="inline-input" v-model="app.notes" :fetch-suggestions="querySearchAsync" placeholder="Please Input"
              @select="handleSelect"></el-autocomplete>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="field">
            <label class="label">SN</label>
            <p class="control">
              <el-input v-model="app.specification.sn" placeholder="Please input"></el-input>
            </p>
          </div>
        </el-col>
      </el-row>
    </card-modal>
  </div>
</template>

<script>

  import { CardModal } from 'vue-bulma-modal'
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
        models: ['5230', '5330', '5340'],
        labs: ['MTV', 'Beijing', 'Chengdu']
      }
    },
    components: {
      CardModal
    },

    computed: {
      modelSelects () {
        return _.map(this.models, (model) => { return {label: model, value: model} })
      },
      labSelects () {
        return _.map(this.labs, (model) => { return {label: model, value: model} })
      }
    },

    methods: {
      querySearchAsync (queryString, cb) {
        cb(this.modelSelects)
      },
      querySearchLabAsync (queryString, cb) {
        cb(this.labSelects)
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
