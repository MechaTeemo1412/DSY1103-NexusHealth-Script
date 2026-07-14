import { useState, useEffect } from 'react';
import axios from 'axios';

function App() {
  const [logs, setLogs] = useState([]);
  const [logsFiltrados, setLogsFiltrados] = useState([]);
  
  // Estados para los filtros
  const [filtroRut, setFiltroRut] = useState('');
  const [filtroEstado, setFiltroEstado] = useState('');

const cargarDatos = async () => {
    try {
      console.log("1. Buscando datos en el backend...");
      
      // ⚠️ Asegúrate de que esta sea la ruta correcta
      const response = await axios.get('/api/v1/auditoria/obtener'); 
      
      console.log("2. Respuesta cruda del backend:", response);

      const datosReales = response.data.content || response.data.data || response.data;
      console.log("3. Datos que intentaremos meter a la tabla:", datosReales);

      if (!Array.isArray(datosReales)) {
        console.error("🚨 ¡CUIDADO! Los datos no son una lista/arreglo válido.");
      }

      setLogs(datosReales);
      setLogsFiltrados(datosReales);
    } catch (error) {
      console.error("🚨 Error grave al conectar con el Gateway:", error);
    }
  };

  useEffect(() => {
    cargarDatos();
  }, []);

  // Lógica de Filtrado en Memoria
  const aplicarFiltros = () => {
    let resultado = logs;

    if (filtroRut) {
      resultado = resultado.filter(log => log.rutPaciente && log.rutPaciente.includes(filtroRut));
    }
    
    if (filtroEstado) {
      resultado = resultado.filter(log => log.estado === filtroEstado);
    }

    setLogsFiltrados(resultado);
  };

  const limpiarFiltros = () => {
    setFiltroRut('');
    setFiltroEstado('');
    setLogsFiltrados(logs);
  };

  // Cálculos para las Tarjetas KPI
  const totalAceptados = logs.filter(l => l.estado === 'EXITO' || l.estado === 'ENVIADO' || l.estado === 'REINTENTANDO').length;
  const totalRechazados = logs.filter(l => l.estado === 'FALLA' || l.estado === 'FALLA_CONTROLADA').length;

  return (
    <div className="container mt-5">
      <h2 className="mb-4 text-center">🩺 NexusHealth - Panel de Monitoreo</h2>

      {/* Tarjetas KPI */}
      <div className="row mb-4">
        <div className="col-md-4">
          <div className="card text-white bg-dark mb-3 shadow-sm">
            <div className="card-body">
              <h5 className="card-title">Total Eventos</h5>
              <p className="card-text fs-1 fw-bold text-center">{logs.length}</p>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card text-white bg-success mb-3 shadow-sm">
            <div className="card-body">
              <h5 className="card-title">Eventos Exitosos</h5>
              <p className="card-text fs-1 fw-bold text-center">{totalAceptados}</p>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card text-white bg-danger mb-3 shadow-sm">
            <div className="card-body">
              <h5 className="card-title">Rechazos / Errores</h5>
              <p className="card-text fs-1 fw-bold text-center">{totalRechazados}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Barra de Filtros */}
      <div className="card mb-4 p-3 shadow-sm bg-light">
        <div className="row g-3 align-items-end">
          <div className="col-md-4">
            <label className="form-label fw-bold">Filtrar por RUT</label>
            <input type="text" className="form-control" placeholder="Ej: 11222333-4" 
                   value={filtroRut} onChange={(e) => setFiltroRut(e.target.value)} />
          </div>
          <div className="col-md-4">
            <label className="form-label fw-bold">Filtrar por Estado</label>
            <select className="form-select" value={filtroEstado} onChange={(e) => setFiltroEstado(e.target.value)}>
              <option value="">Todos los estados</option>
              <option value="PROCESADA">PROCESADA</option>
              <option value="ACEPTADO">ACEPTADO</option>
              <option value="FALLIDA">FALLIDA</option>
              <option value="RECHAZADO">RECHAZADO</option>
            </select>
          </div>
          <div className="col-md-4">
            <button className="btn btn-primary me-2 shadow-sm" onClick={aplicarFiltros}>🔍 Buscar</button>
            <button className="btn btn-secondary me-2 shadow-sm" onClick={limpiarFiltros}>Limpiar</button>
            <button className="btn btn-outline-success shadow-sm" onClick={cargarDatos}>🔄 Refrescar Data</button>
          </div>
        </div>
      </div>

      {/* Tabla de Resultados */}
      <div className="table-responsive shadow-sm rounded">
        <table className="table table-hover table-bordered table-striped mb-0">
          <thead className="table-dark">
            <tr>
              <th>ID Log</th>
              <th>RUT Paciente</th>
              <th>Estado</th>
              <th>Detalle Transacción</th>
            </tr>
          </thead>
          <tbody>
            {logsFiltrados.length > 0 ? (
              logsFiltrados.map((log, index) => (
                <tr key={index}>
                  <td>{log.id || index + 1}</td>
                  <td className="fw-bold">{log.rutPaciente || 'Sistema'}</td>
                  <td>
                    <span className={`badge bg-${(log.estado === 'FALLIDA' || log.estado === 'RECHAZADO') ? 'danger' : 'success'}`}>
                      {log.estado || 'OK'}
                    </span>
                  </td>
                  <td>{log.mensaje || log.detalle || log.descripcion || 'Sin descripción'}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="4" className="text-center p-4 text-muted">No se encontraron registros para estos filtros.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default App;