<template>
  <div>
    <div class="card">
      <header class="card-header">
        <p class="card-header-title">
          {{req.team}},{{req.model}},{{req.quantity}}
        </p>
        <a @click='expanded = !expanded' class="card-header-icon">
          <span v-if="req.status == 'Pending'" class="tag is-primary">
            {{req.status}}
            </span>
          <span v-if="req.status == 'Approved'" class="tag is-success">
            {{req.status}}
            </span>
          <span class="icon">
        <i v-if='expanded' class="fa fa-angle-down"></i>
        <i v-if='!expanded' class="fa fa-angle-up"></i>
      </span>
        </a>
      </header>
      <div v-if='expanded'>
        <div class="card-content">
          <div class="content">
            {{req.description}}
            <a>@{{req.team}}</a>
            <br>
            <span v-if="req.assignedApps.length > 0" class="tag is-primary">
            {{req.assignedApps[0]}}
            </span>
            <span v-if="req.assignedApps.length > 1" class="tag is-primary">
            {{req.assignedApps[1]}}
            </span>
            <span v-if="req.assignedApps.length > 2" class="tag is-primary">
            {{req.assignedApps[2]}}
            </span >
            <span v-if="req.assignedApps.length > 3" class="tag is-primary">
            ...
            </span>
            <br>
            <small>Created at {{req.created}}</small>
          </div>
        </div>
        <footer class="card-footer">
          <a v-if="req.status==='Pending'" @click='handleAssign' class="card-footer-item">Assign</a>
          <a v-if="req.status==='Pending'" @click='handleApprove' class="card-footer-item">Approve</a>
          <a v-if="req.status==='Approved'" @click='handleRevert' class="card-footer-item">Revert</a>
        </footer>
      </div>
    </div>
    <app-req-assign :visible='isEditAppReq' @ok="handleModalClick" @cancel="handleModalClick" @close="handleModalClick"></app-req-assign>
  </div>
</template>

<script>
  import AppReqAssign from './AppReqAssign'
  import _ from 'lodash'
  import moment from 'moment'

  export default {
    props: {
      appReq: {
        type: Object,
        default: () => {
          return {
            status: _.shuffle(['Approved', 'Pending'])[0],
            assignedApps: ['nbapp5a1'],
            team: 'Icebreaker',
            model: '5220',
            description: 'we need some machines',
            quantity: 1,
            created: moment()
          }
        }
      }
    },

    data () {
      return {
        expanded: this.appReq.status === 'Pending',
        isEditAppReq: false,
        req: this.appReq
      }
    },

    components: {
      AppReqAssign
    },

    methods: {
      handleApprove () {
        this.req.status = 'Approved'
      },

      handleRevert () {
        this.req.status = 'Pending'
      },

      handleAssign () {
        this.req.status = 'Pending'
        this.req.assignedApps.push('nbapp5as')
        this.isEditAppReq = true
      },

      handleModalClick () {
        console.log({'approveBtn///': this.approveBtn})
        this.isEditAppReq = false
        console.log({'req.status': this.req.status})
      }
    },

    created () {
      console.log(this.appReq)
    }
  }

</script>

<style lang="scss" scoped>
    @import '~element-ui/lib/theme-default/index.css'
</style>
